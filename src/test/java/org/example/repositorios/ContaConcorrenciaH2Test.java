package org.example.repositorios;

import jakarta.persistence.EntityManager;
import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.entidades.Transacao;
import org.example.objetos.CPF;
import org.example.objetos.TIPO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        properties = {
                "spring.datasource.url=jdbc:h2:mem:contas_testes;DB_CLOSE_DELAY=-1;LOCK_TIMEOUT=10000",
                "spring.datasource.driver-class-name=org.h2.Driver",
                "spring.datasource.username=sa",
                "spring.datasource.password=",
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.flyway.enabled=false"
        }
)

class ContaConcorrenciaH2Test {

    private static final int NUMERO_CONTA = 1001;

    private static final BigDecimal SALDO_INICIAL =
            new BigDecimal("100.00");

    private static final BigDecimal VALOR_DEPOSITO =
            new BigDecimal("10.00");

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private EntityManager entityManager;

    private ExecutorService executor;
    private TransactionTemplate transactionTemplate;

    @BeforeEach
    void prepararBanco() {
        executor = Executors.newFixedThreadPool(2);
        transactionTemplate = new TransactionTemplate(transactionManager);

        /*
         * O preparo também precisa ocorrer em uma transação.
         * Primeiro persistimos o Cliente; depois, a Conta.
         */
        transactionTemplate.executeWithoutResult(status -> {
            Cliente cliente = new Cliente(
                    UUID.randomUUID(),
                    "Cliente de teste",
                    new CPF("52998224725")
            );

            entityManager.persist(cliente);

            Conta conta = new Conta(
                    NUMERO_CONTA,
                    cliente,
                    SALDO_INICIAL
            );

            entityManager.persist(conta);

            /*
             * Garante que Cliente e Conta sejam gravados
             * antes de as threads do teste começarem.
             */
            entityManager.flush();
        });
    }

    @AfterEach
    void encerrarExecutor() {
        if (executor != null) {
            executor.shutdownNow();
        }
    }

    @Test
    void deveGerarConflitoDeVersaoEmDoisDepositosConcorrentes()
            throws Exception {

        /*
         * A primeira trava garante que ambas as transações
         * tenham lido a conta antes de qualquer UPDATE.
         */
        CountDownLatch contasCarregadas = new CountDownLatch(2);

        /*
         * A segunda trava libera as duas operações para
         * tentarem persistir ao mesmo tempo.
         */
        CountDownLatch liberarAtualizacao = new CountDownLatch(1);

        Future<Void> primeiraOperacao = executor.submit(() -> {
            executarDepositoConcorrente(
                    contasCarregadas,
                    liberarAtualizacao
            );
            return null;
        });

        Future<Void> segundaOperacao = executor.submit(() -> {
            executarDepositoConcorrente(
                    contasCarregadas,
                    liberarAtualizacao
            );
            return null;
        });

        /*
         * Aguarda as duas transações carregarem a mesma conta,
         * portanto, as duas terão lido a mesma versao.
         */
        assertTrue(
                contasCarregadas.await(5, TimeUnit.SECONDS),
                "As duas operações não carregaram a conta a tempo"
        );

        /*
         * Libera ambas as threads para executar o flush.
         */
        liberarAtualizacao.countDown();

        int operacoesComSucesso = 0;
        List<Throwable> erros = new ArrayList<>();

        for (Future<Void> operacao : List.of(
                primeiraOperacao,
                segundaOperacao
        )) {
            try {
                operacao.get(10, TimeUnit.SECONDS);
                operacoesComSucesso++;
            } catch (ExecutionException e) {
                erros.add(e.getCause());
            }
        }

        /*
         * Apenas uma operação deve conseguir atualizar a conta.
         */
        assertEquals(1, operacoesComSucesso);

        /*
         * A outra deve falhar pelo controle otimista do @Version.
         */
        assertEquals(1, erros.size());

        assertTrue(
                possuiCausa(
                        erros.get(0),
                        OptimisticLockingFailureException.class
                ),
                "Era esperado um conflito otimista, mas ocorreu: "
                        + erros.get(0)
        );

        Conta contaFinal = contaRepository.findById(NUMERO_CONTA)
                .orElseThrow();

        /*
         * Só um depósito de R$ 10,00 foi confirmado:
         * 100,00 + 10,00 = 110,00.
         */
        assertEquals(
                new BigDecimal("110.00"),
                contaFinal.getSaldo()
        );

        /*
         * A transação da operação que falhou também sofre rollback.
         * Por isso, apenas uma transação fica gravada.
         */
        assertEquals(1, transacaoRepository.count());
    }

    private void executarDepositoConcorrente(
            CountDownLatch contasCarregadas,
            CountDownLatch liberarAtualizacao
    ) {
        transactionTemplate.executeWithoutResult(status -> {
            Conta conta = contaRepository.findById(NUMERO_CONTA)
                    .orElseThrow();

            /*
             * Ambas as threads calculam:
             * saldo: 100,00 -> 110,00
             *
             * Porém, isso ainda está apenas no contexto
             * de persistência daquela transação.
             */
            conta.creditar(VALOR_DEPOSITO);

            /*
             * Grava a transação dentro da mesma transação do saldo.
             * Se houver falha no flush da conta, tudo sofre rollback.
             */
            Transacao transacao = new Transacao(
                    UUID.randomUUID(),
                    TIPO.DEPOSITO,
                    VALOR_DEPOSITO,
                    LocalDateTime.now(),
                    conta
            );

            transacaoRepository.save(transacao);

            /*
             * Informa ao teste que esta thread já carregou
             * a conta e alterou seu saldo em memória.
             */
            contasCarregadas.countDown();

            try {
                boolean foiLiberada = liberarAtualizacao.await(
                        5,
                        TimeUnit.SECONDS
                );

                if (!foiLiberada) {
                    throw new IllegalStateException(
                            "Tempo excedido aguardando atualização concorrente"
                    );
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();

                throw new IllegalStateException(
                        "Thread interrompida durante o teste",
                        e
                );
            }

            /*
             * Força o Hibernate a executar os comandos SQL agora.
             *
             * Uma thread atualizará a linha da conta.
             * A outra tentará atualizar usando a versão antiga
             * e receberá OptimisticLockingFailureException.
             */
            contaRepository.flush();
        });
    }

    private boolean possuiCausa(
            Throwable erro,
            Class<? extends Throwable> tipoEsperado
    ) {
        Throwable causaAtual = erro;

        while (causaAtual != null) {
            if (tipoEsperado.isInstance(causaAtual)) {
                return true;
            }

            causaAtual = causaAtual.getCause();
        }

        return false;
    }
}
