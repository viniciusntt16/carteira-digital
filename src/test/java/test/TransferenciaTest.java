package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.entidades.Transacao;
import org.example.exceptions.ContaNaoEncontradaException;
import org.example.exceptions.SaldoInsuficienteException;
import org.example.objetos.CPF;
import org.example.objetos.TIPO;
import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoEmMemoria;
import org.example.repositorios.TransacaoRepository;
import org.example.servicos.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransferenciaTest {
    private ContaRepository contaRepository;
    private TransacaoRepository transacaoRepository;
    private ContaService contaService;
    private Cliente c1;
    private Cliente c2;
    private Conta conta;
    private Conta conta2;

    @BeforeEach
    void preparar(){
        contaRepository = mock(ContaRepository.class);
        transacaoRepository = new TransacaoEmMemoria();
        contaService = new ContaService(contaRepository, transacaoRepository);
        c1 = new Cliente(UUID.randomUUID(), "Vinicius",
                new CPF("12345678910"));
        c2 = new Cliente(UUID.randomUUID(), "Peres",
                new CPF("12345678911"));
        conta = new Conta(1, c1, BigDecimal.valueOf(1000));
        conta2 = new Conta(2, c2, BigDecimal.valueOf(2000));
        when(contaRepository.findById(1)).thenReturn(Optional.of(conta));
        when(contaRepository.findById(2)).thenReturn(Optional.of(conta2));

    }

    @Test
    void transferencia(){
        contaService.transferir(2, 1, BigDecimal.valueOf(1500));
        assertEquals(BigDecimal.valueOf(2500)
                .setScale(2, RoundingMode.HALF_EVEN), conta.getSaldo());
        assertEquals(BigDecimal.valueOf(500)
                .setScale(2, RoundingMode.HALF_EVEN), conta2.getSaldo());

        List<Transacao> transacaos = transacaoRepository.lista();
        assertEquals(1, transacaos.size());
        assertEquals(TIPO.TRANSFERENCIA, transacaos.get(0).getTipo());
    }

    @Test
    void transferenciaValorIncorreto(){
        assertThrows(IllegalArgumentException.class,
                ()->contaService.transferir(1, 2,BigDecimal.valueOf(0)));
        assertThrows(IllegalArgumentException.class,
                ()->contaService.transferir(1, 2,BigDecimal.valueOf(-1)));
    }

    @Test
    void transferenciaParaContaNaoEncotrada(){
        assertThrows(ContaNaoEncontradaException.class,
                ()->contaService.transferir(1, 3, BigDecimal.valueOf(500)));
        assertEquals(BigDecimal.valueOf(1000)
                .setScale(2, RoundingMode.HALF_EVEN), conta.getSaldo());
    }

    @Test
    void transferenciaException(){
        assertThrows(SaldoInsuficienteException.class,
                ()->contaService.transferir(2, 1, BigDecimal.valueOf(2500)));
        assertEquals(BigDecimal.valueOf(1000)
                .setScale(2,RoundingMode.HALF_EVEN), conta.getSaldo());
        assertEquals(BigDecimal.valueOf(2000)
                .setScale(2, RoundingMode.HALF_EVEN), conta2.getSaldo());
    }

    @Test
    void transferenciaNaoRegistraErro(){
        assertThrows(ContaNaoEncontradaException.class,
                ()->contaService.transferir(1, 3, BigDecimal.valueOf(500)));
        assertEquals(0, transacaoRepository.lista().size());
    }

    @Test
    void transferenciaParaAPropriaConta(){
        assertThrows(IllegalArgumentException.class,
                ()->contaService.transferir(1,1,BigDecimal.valueOf(500)));
    }
}
