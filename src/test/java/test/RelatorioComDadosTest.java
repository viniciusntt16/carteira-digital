package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.entidades.Transacao;
import org.example.objetos.CPF;
import org.example.objetos.TIPO;
import org.example.repositorios.ContaEmMemoria;
import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoEmMemoria;
import org.example.repositorios.TransacaoRepository;
import org.example.servicos.ContaService;
import org.example.servicos.RelatorioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

public class RelatorioComDadosTest {
    private ContaRepository contaRepository;
    private TransacaoRepository transacaoRepository;
    private RelatorioService relatorioService;
    private ContaService contaService;
    private Cliente c1;
    private Cliente c2;
    private Cliente c3;
    private Conta conta;
    private Conta conta2;
    private Conta conta3;

    @BeforeEach
    void preparar() throws InterruptedException {
        contaRepository = new ContaEmMemoria();
        transacaoRepository = new TransacaoEmMemoria();
        contaService = new ContaService(contaRepository, transacaoRepository);
        c1 = new Cliente(UUID.randomUUID(), "Vinicius",
                new CPF("12345678910"));
        c2 = new Cliente(UUID.randomUUID(), "Peres",
                new CPF("12345678911"));
        conta = new Conta(1, c1, BigDecimal.valueOf(1000));
        conta2 = new Conta(2, c2, BigDecimal.valueOf(2000));
        conta3 = new Conta(3, c1, BigDecimal.valueOf(2000));
        contaRepository.salvar(conta);
        contaRepository.salvar(conta2);
        contaRepository.salvar(conta3);
        relatorioService = new RelatorioService(
                contaRepository,
                transacaoRepository
        );
        contaService.depositar(1, BigDecimal.valueOf(1000));
        Thread.sleep(300);
        contaService.transferir(2, 1, BigDecimal.valueOf(500));
        Thread.sleep(300);
        contaService.depositar(1, BigDecimal.valueOf(500));
        Thread.sleep(300);
        contaService.sacar(1, BigDecimal.valueOf(500));
        Thread.sleep(300);
        contaService.depositar(2, BigDecimal.valueOf(100));
    }

    @Test
    void extrato(){
        List<Transacao> extrato = relatorioService.extrato(1);
        assertEquals(4, extrato.size());
        assertEquals(TIPO.SAQUE, extrato.get(0).getTipo());
        assertEquals(TIPO.DEPOSITO, extrato.get(1).getTipo());
        assertEquals(TIPO.TRANSFERENCIA, extrato.get(2).getTipo());
    }

    @Test
    void totalPorTipo(){
        Map<TIPO, BigDecimal> deposito = relatorioService.totalPorTipo();
        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_EVEN),
                deposito.get(TIPO.TRANSFERENCIA));
        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_EVEN),
                deposito.get(TIPO.SAQUE));
        assertEquals(BigDecimal.valueOf(1600).setScale(2, RoundingMode.HALF_EVEN),
                deposito.get(TIPO.DEPOSITO));
    }

    @Test
    void clienteComMaiorSaldo(){
        Optional<Cliente> cliente = relatorioService.clienteComMaiorSaldo();
        assertSame(c1, cliente.orElseThrow());
    }

    @Test
    void contasSemMovimento(){
        List<Conta> contas = relatorioService.contasSemMovimentacao();
        assertSame(conta3, contas.get(0));
    }

    @Test
    void saldoTotalPorCliente(){
        Map<Cliente, BigDecimal> saldo = relatorioService.saldoTotalPorCliente();
        assertEquals(BigDecimal.valueOf(4500).setScale(2, RoundingMode.HALF_EVEN),
                saldo.get(c1));
        assertEquals(BigDecimal.valueOf(1600).setScale(2, RoundingMode.HALF_EVEN),
                saldo.get(c2));
    }
}
