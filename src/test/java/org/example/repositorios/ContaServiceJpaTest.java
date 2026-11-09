package org.example.repositorios;

import org.example.entidades.Transacao;
import org.example.objetos.TIPO;
import org.junit.jupiter.api.AfterEach;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.objetos.CPF;
import org.example.servicos.ContaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@DataJpaTest(showSql = false)
@Import(ContaService.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ContaServiceJpaTest {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private  ContaRepository contaRepository;
    @Autowired
    private ContaService contaService;
    @Autowired
    private TransacaoRepository transacaoRepository;

    @AfterEach
    void limparBanco() {
        transacaoRepository.deleteAll();
        contaRepository.deleteAll();
        clienteRepository.deleteAll();
    }


    @Test
    void depositarPersisteSaldoNoBanco() {
        UUID clienteId = UUID.randomUUID();
        int numeroConta = 1001;

        Cliente cliente = new Cliente(
                clienteId,
                "Vinicius",
                new CPF("12345678910")
        );

        clienteRepository.saveAndFlush(cliente);
        Conta conta = new Conta(numeroConta, cliente, BigDecimal.valueOf(1000));
        contaRepository.saveAndFlush(conta);
        contaService.depositar(numeroConta, BigDecimal.valueOf(500));

        Conta contaAtualizada = contaRepository.findById(numeroConta)
                .orElseThrow();

        assertEquals(0, new BigDecimal("1500")
                .compareTo(contaAtualizada.getSaldo())
        );
        List<Transacao> transacoes = transacaoRepository.findAll();

        assertEquals(1, transacoes.size());

        Transacao transacaoSalva = transacoes.get(0);

        assertEquals(TIPO.DEPOSITO, transacaoSalva.getTipo());
        assertEquals(0, new BigDecimal("500")
                .compareTo(transacaoSalva.getValor()));
    }

    @Test
    void transferenciaPersisteSaldoNoBanco() {
        UUID clienteId = UUID.randomUUID();
        UUID clienteId2 = UUID.randomUUID();
        int numeroConta = 1001;
        int numeroConta2 = 1002;

        Cliente cliente = new Cliente(
                clienteId,
                "Vinicius",
                new CPF("12345678910")
        );
        Cliente cliente2 = new Cliente(
                clienteId2,
                "Peres",
                new CPF("12345678911")
        );

        clienteRepository.saveAndFlush(cliente);
        clienteRepository.saveAndFlush(cliente2);

        Conta conta = new Conta(numeroConta, cliente, BigDecimal.valueOf(1000));
        Conta conta2 = new Conta(numeroConta2, cliente2, BigDecimal.valueOf(1000));

        contaRepository.saveAndFlush(conta);
        contaRepository.saveAndFlush(conta2);

        contaService.transferir(numeroConta, numeroConta2, BigDecimal.valueOf(500));

        Conta origemAtualizada = contaRepository.findById(numeroConta).orElseThrow();
        Conta destinoAtualizada = contaRepository.findById(numeroConta2).orElseThrow();

        assertEquals(0, new BigDecimal("500")
                .compareTo(origemAtualizada.getSaldo())
        );

        assertEquals(0, new BigDecimal("1500")
                .compareTo(destinoAtualizada.getSaldo())
        );
        List<Transacao> transacoes = transacaoRepository.findAll();
        assertEquals(1, transacoes.size());

        Transacao transacaoSalva = transacoes.get(0);
        assertEquals(TIPO.TRANSFERENCIA, transacaoSalva.getTipo());
        assertEquals(0, new BigDecimal("500")
                .compareTo(transacaoSalva.getValor()));
    }

    @Test
    void buscarEOrdenar() {
        Cliente cliente = new Cliente(
                UUID.randomUUID(),
                "Vinicius",
                new CPF("12345678910")
        );
        Cliente cliente2 = new Cliente(
                UUID.randomUUID(),
                "Peres",
                new CPF("12345678911")
        );

        clienteRepository.saveAndFlush(cliente);
        clienteRepository.saveAndFlush(cliente2);

        Conta conta = new Conta(1, cliente, BigDecimal.valueOf(1000));
        Conta conta2 = new Conta(2, cliente2, BigDecimal.valueOf(1000));

        contaRepository.saveAndFlush(conta);
        contaRepository.saveAndFlush(conta2);

        Transacao depositoConta1 = new Transacao(
                UUID.randomUUID(),
                TIPO.DEPOSITO,
                new BigDecimal("100.00"),
                LocalDateTime.of(2026, 1, 10, 10, 0),
                conta
        );

        Transacao recebidaPorConta1 = new Transacao(
                UUID.randomUUID(),
                TIPO.TRANSFERENCIA,
                new BigDecimal("200.00"),
                LocalDateTime.of(2026, 1, 10, 11, 0),
                conta2,
                conta
        );

        Transacao depositoConta2 = new Transacao(
                UUID.randomUUID(),
                TIPO.DEPOSITO,
                new BigDecimal("300.00"),
                LocalDateTime.of(2026, 1, 10, 12, 0),
                conta2
        );

        Transacao enviadaPorConta1 = new Transacao(
                UUID.randomUUID(),
                TIPO.TRANSFERENCIA,
                new BigDecimal("400.00"),
                LocalDateTime.of(2026, 1, 10, 13, 0),
                conta,
                conta2
        );
        transacaoRepository.saveAllAndFlush(List.of(
                depositoConta1,
                recebidaPorConta1,
                depositoConta2,
                enviadaPorConta1
        ));

        List<Transacao> resultado = transacaoRepository
                .buscarPorContaOrdenadoPorData(1);

        List<UUID> idsEncontrados = resultado.stream()
                .map(Transacao::getId)
                .toList();

        assertEquals(List.of(
                enviadaPorConta1.getId(),
                recebidaPorConta1.getId(),
                depositoConta1.getId()
        ), idsEncontrados);
    }

}
