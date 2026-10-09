package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.entidades.Transacao;
import org.example.exceptions.SaldoInsuficienteException;
import org.example.objetos.CPF;
import org.example.objetos.TIPO;
import org.example.repositorios.ContaEmMemoria;
import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoEmMemoria;
import org.example.repositorios.TransacaoRepository;
import org.example.servicos.ContaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SaqueTest {
    private ContaRepository contaRepository;
    private TransacaoRepository transacaoRepository;
    private ContaService contaService;
    private Cliente cliente;
    private Conta conta;
    @BeforeEach
    void preparar(){
        contaRepository = new ContaEmMemoria();
        transacaoRepository = new TransacaoEmMemoria();
        contaService = new ContaService(contaRepository, transacaoRepository);
        cliente = new Cliente(UUID.randomUUID(), "Vinicius",
                new CPF("12345678910"));
        conta = new Conta(1, cliente, BigDecimal.valueOf(1000));
        contaRepository.salvar(conta);
    }

    @Test
    void saqueTeste(){
        contaService.sacar(1, BigDecimal.valueOf(500));
        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_EVEN), conta.getSaldo());

        List<Transacao> transacaos = transacaoRepository.lista();
        assertEquals(1, transacaos.size());
        assertEquals(TIPO.SAQUE, transacaos.get(0).getTipo());
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
        assertEquals(0, transacaoRepository.lista().size());
    }
}
