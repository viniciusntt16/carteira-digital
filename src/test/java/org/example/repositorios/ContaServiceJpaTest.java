package org.example.repositorios;

import org.example.dtos.ContaRequest;
import org.example.dtos.ContaResponse;
import org.example.dtos.TransacaoResponse;
import org.example.entidades.Transacao;
import org.example.exceptions.ClienteNaoEncontradoException;
import org.example.exceptions.ContaDuplicadaException;
import org.example.exceptions.ContaNaoEncontradaException;
import org.example.objetos.TIPO;
import org.example.servicos.RelatorioService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.objetos.CPF;
import org.example.servicos.ContaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;


import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(showSql = false)
@Import({
        ContaService.class,
        RelatorioService.class,
        ContaServiceJpaTest.ConfiguracaoClockDeTeste.class
})


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

    private static final int numeroConta = 1001;

    private UUID clienteId;
    private Cliente cliente;
    private Conta conta;

    @BeforeEach
    void preparar() {
        clienteId = UUID.randomUUID();
        cliente = new Cliente(
                clienteId,
                "Vinicius",
                new CPF("52998224725")
        );

        cliente = clienteRepository.saveAndFlush(cliente);

        conta = new Conta(
                numeroConta,
                cliente,
                new BigDecimal("1000.00")
        );

        conta = contaRepository.saveAndFlush(conta);
    }

    @Test
    void depositarPersisteSaldoNoBanco() {
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
        UUID clienteId2 = UUID.randomUUID();
        int numeroConta2 = 1002;
        Cliente cliente2 = new Cliente(
                clienteId2,
                "Peres",
                new CPF("12345678911")
        );

        clienteRepository.saveAndFlush(cliente2);
        Conta conta2 = new Conta(numeroConta2, cliente2, BigDecimal.valueOf(1000));

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
        Cliente cliente2 = new Cliente(
                UUID.randomUUID(),
                "Peres",
                new CPF("12345678911")
        );
        clienteRepository.saveAndFlush(cliente2);
        Conta conta2 = new Conta(2, cliente2, BigDecimal.valueOf(1000));
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
                .buscarPorContaOrdenadoPorData(numeroConta);

        List<UUID> idsEncontrados = resultado.stream()
                .map(Transacao::getId)
                .toList();

        assertEquals(List.of(
                enviadaPorConta1.getId(),
                recebidaPorConta1.getId(),
                depositoConta1.getId()
        ), idsEncontrados);
    }

    @Test
    void criarPersisteContaNoBanco(){
        int numeroConta2 = 1002;

        ContaRequest request = new ContaRequest(
                numeroConta2,
                clienteId,
                new BigDecimal("1000.00")
        );
        ContaResponse response = contaService.criar(request);
        assertEquals(numeroConta2, response.numero());
        assertEquals(clienteId, response.clienteId());
        assertEquals(0, new BigDecimal("1000.00").compareTo(response.saldo()));

        Conta contaSalva = contaRepository.findById(numeroConta2)
                .orElseThrow();

        assertEquals(numeroConta2, contaSalva.getNumero());
        assertEquals(0, new BigDecimal("1000.00").compareTo(contaSalva.getSaldo()));
    }

    @Test
    void criaContaNaoPermiteNumeroDeContaDuplicado(){
        ContaRequest request = new ContaRequest(
                numeroConta,
                clienteId,
                new BigDecimal("1000.00")
        );

        assertThrows(ContaDuplicadaException.class,
                () -> contaService.criar(request));
        Conta contaSalva = contaRepository.findById(numeroConta)
                .orElseThrow();
        assertEquals(0, new BigDecimal("1000.00")
                .compareTo(contaSalva.getSaldo()));
    }

    @Test
    void criarNaoPermiteClienteInexistente(){
        int numeroConta2 = 1002;
        UUID clienteInexistente = UUID.randomUUID();
        ContaRequest request = new ContaRequest(numeroConta2,
                clienteInexistente,
                new BigDecimal("1000.00"));

        assertThrows(ClienteNaoEncontradoException.class,
                ()->contaService.criar(request));
        assertFalse(contaRepository.existsById(numeroConta2));
    }

    @Test
    void buscarPorNumeroRetornaConta(){
        ContaResponse response = contaService.buscarPorNumero(numeroConta);
        assertEquals(numeroConta, response.numero());
        assertEquals(clienteId, response.clienteId());
        assertEquals(0, new BigDecimal("1000.00")
                .compareTo(response.saldo()));
    }

    @Test
    void buscarPorNumeroInexistenteLancaException(){
        int numeroConta2 = 9999;
        assertThrows(ContaNaoEncontradaException.class,
                ()->contaService.buscarPorNumero(numeroConta2));
    }

    @Test
    void buscarTransacoesRetornaExtratoVazioParaContaSemMovimentacao() {
        List<TransacaoResponse> extrato =
                contaService.buscarTransacoesPorConta(numeroConta);

        assertTrue(extrato.isEmpty());
    }

    @Test
    void buscarTransacoesRetornaDepositoComoDto() {
        contaService.depositar(numeroConta, new BigDecimal("150.00"));

        List<TransacaoResponse> extrato =
                contaService.buscarTransacoesPorConta(numeroConta);

        assertEquals(1, extrato.size());

        TransacaoResponse transacao = extrato.get(0);

        assertEquals(TIPO.DEPOSITO, transacao.tipo());
        assertEquals(
                0,
                new BigDecimal("150.00").compareTo(transacao.valor())
        );
        assertEquals(
                LocalDateTime.of(2026, 1, 10, 14, 30),
                transacao.dataHora()
        );
    }

    @Test
    void buscarTransacoesLancaExcecaoQuandoContaNaoExiste() {
        int numeroInexistente = 9999;

        assertThrows(
                ContaNaoEncontradaException.class,
                () -> contaService.buscarTransacoesPorConta(numeroInexistente)
        );
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class ConfiguracaoClockDeTeste {

        @Bean
        Clock clock() {
            return Clock.fixed(
                    Instant.parse("2026-01-10T14:30:00Z"),
                    ZoneOffset.UTC
            );
        }
    }
}
