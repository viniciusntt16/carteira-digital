package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.entidades.Transacao;
import org.example.exceptions.ContaNaoEncontradaException;
import org.example.objetos.CPF;
import org.example.objetos.TIPO;
import org.example.repositorios.ContaEmMemoria;
import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoEmMemoria;
import org.example.repositorios.TransacaoRepository;
import org.example.servicos.RelatorioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class RelatorioVazioTest {

    private ContaRepository contaRepository;
    private TransacaoRepository transacaoRepository;
    private RelatorioService relatorioService;

    @BeforeEach
    void preparar(){
        contaRepository = new ContaEmMemoria();
        transacaoRepository = new TransacaoEmMemoria();
        relatorioService = new RelatorioService(
                contaRepository,
                transacaoRepository
        );
    }

    @Test
    void extrato(){
        List<Transacao> extrato = relatorioService.extrato(1);
        assertTrue(extrato.isEmpty());
    }

    @Test
    void totalPorTipo(){
        Map<TIPO, BigDecimal> deposito = relatorioService.totalPorTipo();
        assertTrue(deposito.isEmpty());
    }

    @Test
    void clienteComMaiorSaldo(){
        Optional<Cliente> cliente = relatorioService.clienteComMaiorSaldo();
        assertTrue(cliente.isEmpty());
    }

    @Test
    void contasSemMovimento(){
        List<Conta> contas = relatorioService.contasSemMovimentacao();
        assertTrue(contas.isEmpty());
    }

    @Test
    void saldoTotalPorCliente(){
        Map<Cliente, BigDecimal> saldo = relatorioService.saldoTotalPorCliente();
        assertTrue(saldo.isEmpty());
    }
}
