package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.objetos.CPF;
import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoRepository;
import org.example.servicos.RelatorioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

public class RelatorioComDadosContinuacaoTest {
    private ContaRepository contaRepository;
    private TransacaoRepository transacaoRepository;
    private RelatorioService relatorioService;
    private Cliente c1;
    private Cliente c2;
    private Conta conta;
    private Conta conta2;
    private Conta conta3;

    @BeforeEach
    void preparar() {
        contaRepository = mock(ContaRepository.class);
        transacaoRepository = mock(TransacaoRepository.class);
        c1 = new Cliente(UUID.randomUUID(), "Vinicius",
                new CPF("12345678910"));
        c2 = new Cliente(UUID.randomUUID(), "Peres",
                new CPF("12345678911"));
        conta = new Conta(1, c1, BigDecimal.valueOf(1000));
        conta2 = new Conta(2, c2, BigDecimal.valueOf(2000));
        conta3 = new Conta(3, c1, BigDecimal.valueOf(2000));

        conta.creditar(new BigDecimal("1000.00"));
        conta2.debitar(new BigDecimal("500.00"));
        conta.creditar(new BigDecimal("500.00"));
        conta.creditar(new BigDecimal("500.00"));
        conta.debitar(new BigDecimal("500.00"));
        conta2.creditar(new BigDecimal("100.00"));

        when(contaRepository.findById(1)).thenReturn(Optional.of(conta));
        when(contaRepository.findById(2)).thenReturn(Optional.of(conta2));
        when(contaRepository.findById(3)).thenReturn(Optional.of(conta3));

        when(contaRepository.findAll())
                .thenReturn(List.of(conta, conta2, conta3));

        when(contaRepository.buscarContasSemMovimentacao())
                .thenReturn(List.of(conta3));

        relatorioService = new RelatorioService(
                contaRepository,
                transacaoRepository
        );
    }

    @Test
    void clienteComMaiorSaldo(){
        Optional<Cliente> cliente = relatorioService.clienteComMaiorSaldo();
        assertSame(c1, cliente.orElseThrow());
        verify(contaRepository).findAll();
    }

    @Test
    void contasSemMovimento(){
        List<Conta> contas = relatorioService.contasSemMovimentacao();
        assertEquals(1, contas.size());
        assertSame(conta3, contas.get(0));

        verify(contaRepository).buscarContasSemMovimentacao();
        verify(contaRepository, never()).findAll();
        verify(transacaoRepository, never()).findAll();
    }

    @Test
    void saldoTotalPorCliente(){
        Map<Cliente, BigDecimal> saldo = relatorioService.saldoTotalPorCliente();
        assertEquals(BigDecimal.valueOf(4500).setScale(2, RoundingMode.HALF_EVEN),
                saldo.get(c1));
        assertEquals(BigDecimal.valueOf(1600).setScale(2, RoundingMode.HALF_EVEN),
                saldo.get(c2));
        assertEquals(2, saldo.size());
        verify(contaRepository).findAll();

    }
}
