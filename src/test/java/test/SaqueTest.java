package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.entidades.Transacao;
import org.example.exceptions.SaldoInsuficienteException;
import org.example.objetos.CPF;
import org.example.objetos.TIPO;
import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoRepository;
import org.example.servicos.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class SaqueTest {
    private ContaRepository contaRepository;
    private TransacaoRepository transacaoRepository;
    private ContaService contaService;
    private Cliente cliente;
    private Conta conta;
    @BeforeEach
    void preparar(){
        contaRepository = mock(ContaRepository.class);
        transacaoRepository = mock(TransacaoRepository.class);
        contaService = new ContaService(contaRepository, transacaoRepository);
        cliente = new Cliente(UUID.randomUUID(), "Vinicius",
                new CPF("12345678910"));
        conta = new Conta(1, cliente, BigDecimal.valueOf(1000));
        when(contaRepository.findById(1)).thenReturn(Optional.of(conta));
    }

    @Test
    void saqueTeste(){
        contaService.sacar(1, BigDecimal.valueOf(500));
        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_EVEN),
                conta.getSaldo());

        ArgumentCaptor<Transacao> captor = ArgumentCaptor.forClass(Transacao.class);
        verify(transacaoRepository).save(captor.capture());
        Transacao transacaoSalva = captor.getValue();
        assertEquals(TIPO.SAQUE, transacaoSalva.getTipo());
        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_EVEN),
                transacaoSalva.getValor());
    }

    @Test
    void saqueValorIncorreto(){
        contaService.depositar(1, BigDecimal.valueOf(500));

        assertThrows(IllegalArgumentException.class,
                ()->contaService.sacar(1, BigDecimal.valueOf(0)));
        assertThrows(IllegalArgumentException.class,
                ()->contaService.sacar(1, BigDecimal.valueOf(-1)));
    }

    @Test
    void saqueNaoRegistraErro(){
        assertThrows(SaldoInsuficienteException.class,
                ()->contaService.sacar(1,BigDecimal.valueOf(1500)));
        assertEquals(0, transacaoRepository.findAll().size());
    }
}
