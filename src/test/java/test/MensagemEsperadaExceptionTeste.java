package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.exceptions.ClienteDuplicadoException;
import org.example.exceptions.ContaNaoEncontradaException;
import org.example.exceptions.SaldoInsuficienteException;
import org.example.objetos.CPF;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MensagemEsperadaExceptionTeste {
    @Test
    void clienteDuplicado(){
        Cliente c1 = new Cliente(UUID.randomUUID(),
                "Vinicius", new CPF("12345678910"));
        ClienteDuplicadoException exception = new ClienteDuplicadoException(c1.getCpf());
        assertEquals("Já existe um cliente com o cpf 12345678910 cadastrado",
                exception.getMessage());
    }
    @Test
    void contaNaoEncontrada(){
        Cliente c1 = new Cliente(UUID.randomUUID(),
                "Vinicius", new CPF("12345678910"));
        Conta conta = new Conta(1, c1, 2000);
        ContaNaoEncontradaException exception = new ContaNaoEncontradaException(conta.getNumero());
        assertEquals("Conta com o id 1 não foi encontrada",
                exception.getMessage());
    }
    @Test
    void saldoInsuficiente(){
        SaldoInsuficienteException exception = new SaldoInsuficienteException(1, 2000, 1500);
        assertEquals("A conta de numero 1 não pode realizar uma operação neste valor:2000.0. " +
                "O saldo é 1500.0, digite um valor igual ou menor que esse.", exception.getMessage());
    }
}
