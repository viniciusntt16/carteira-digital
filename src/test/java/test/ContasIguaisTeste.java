package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.objetos.CPF;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ContasIguaisTeste {
    @Test
    public void deveRetornarContasIguais(){
        Cliente c1 = new Cliente(UUID.randomUUID(),"Vinicius",new CPF("12345678910"));
        Cliente c2 = new Cliente(UUID.randomUUID(), "Peres", new CPF("12345678910"));

        Conta conta1 = new Conta(
                1, c1, 1000
        );
        Conta conta2 = new Conta(
                1, c2, 1500
        );
        assertEquals(conta1, conta2);
    }
}
