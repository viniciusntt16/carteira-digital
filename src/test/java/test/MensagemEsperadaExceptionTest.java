package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.exceptions.ClienteDuplicadoException;
import org.example.exceptions.ContaNaoEncontradaException;
import org.example.exceptions.SaldoInsuficienteException;
import org.example.objetos.CPF;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MensagemEsperadaExceptionTest {
    private Cliente c1;
    private Conta conta;
    @BeforeEach
    void preparar(){
        c1 = new Cliente(UUID.randomUUID(),
                "Vinicius", new CPF("12345678910"));
        conta = new Conta(1, c1, BigDecimal.valueOf(2000));
    }
    @Test
    void clienteDuplicado(){
        ClienteDuplicadoException exception = new ClienteDuplicadoException(c1.getCpf());
        assertEquals("Já existe um cliente com o cpf 12345678910 cadastrado",
                exception.getMessage());
    }
    @Test
    void contaNaoEncontrada(){
        ContaNaoEncontradaException exception = new ContaNaoEncontradaException(conta.getNumero());
        assertEquals("Conta com o id 1 não foi encontrada",
                exception.getMessage());
    }
    @Test
    void saldoInsuficiente(){
        SaldoInsuficienteException exception = new SaldoInsuficienteException(1,
                BigDecimal.valueOf(2000), BigDecimal.valueOf(1500));
        assertEquals("A conta de numero 1 não pode realizar uma operação neste valor:2000. " +
                "O saldo é 1500, digite um valor igual ou menor que esse.", exception.getMessage());
    }
}
