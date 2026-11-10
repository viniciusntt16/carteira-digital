package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.entidades.Transacao;
import org.example.exceptions.ContaNaoEncontradaException;
import org.example.objetos.CPF;
import org.example.objetos.TIPO;
import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoRepository;
import org.example.servicos.RelatorioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RelatorioVazioTest {

    private ContaRepository contaRepository;
    private TransacaoRepository transacaoRepository;
    private RelatorioService relatorioService;

    @BeforeEach
    void preparar(){
        contaRepository = mock(ContaRepository.class);
        transacaoRepository = mock(TransacaoRepository.class);
        relatorioService = new RelatorioService(
                contaRepository,
                transacaoRepository
        );
    }

    @Test
    void extrato(){
        assertThrows(ContaNaoEncontradaException.class,
                () -> relatorioService.extrato(1));
    }

    @Test
    void extratoDeContaSemTransacoes() {
        Cliente cliente = new Cliente(
                UUID.randomUUID(),
                "Vinicius",
                new CPF("12345678910")
        );

        Conta conta = new Conta(
                1,
                cliente,
                new BigDecimal("1000.00")
        );

        when(contaRepository.findById(1))
                .thenReturn(Optional.of(conta));

        when(transacaoRepository.buscarPorContaOrdenadoPorData(1))
                .thenReturn(List.of());

        List<Transacao> extrato = relatorioService.extrato(1);

        assertTrue(extrato.isEmpty());

        verify(contaRepository).findById(1);
        verify(transacaoRepository).buscarPorContaOrdenadoPorData(1);

        verify(transacaoRepository, never())
                .save(any(Transacao.class));
    }

    @Test
    void totalPorTipo(){
        when(transacaoRepository.findAll())
                .thenReturn(List.of());

        Map<TIPO, BigDecimal> total = relatorioService.totalPorTipo();

        assertTrue(total.isEmpty());

        verify(transacaoRepository).findAll();
    }

    @Test
    void clienteComMaiorSaldo(){
        when(contaRepository.findAll())
                .thenReturn(List.of());

        Optional<Cliente> cliente = relatorioService.clienteComMaiorSaldo();

        assertTrue(cliente.isEmpty());

        verify(contaRepository).findAll();
    }

    @Test
    void contasSemMovimento(){
        when(contaRepository.buscarContasSemMovimentacao())
                .thenReturn(List.of());

        List<Conta> contas = relatorioService.contasSemMovimentacao();

        assertTrue(contas.isEmpty());

        verify(contaRepository).buscarContasSemMovimentacao();
        verify(transacaoRepository, never()).findAll();
        verify(contaRepository, never()).findAll();
    }

    @Test
    void saldoTotalPorCliente(){
        when(contaRepository.findAll())
                .thenReturn(List.of());

        Map<Cliente, BigDecimal> saldo = relatorioService.saldoTotalPorCliente();

        assertTrue(saldo.isEmpty());

        verify(contaRepository).findAll();
    }
}
