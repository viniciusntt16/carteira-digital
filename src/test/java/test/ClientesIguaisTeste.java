package test;

import org.example.entidades.Cliente;
import org.example.objetos.CPF;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientesIguaisTeste {
    @Test
    void deveRetornarClientesIguais(){
        Cliente c1 = new Cliente(UUID.randomUUID(),"Vinicius",new CPF("12345678910"));
        Cliente c2 = new Cliente(UUID.randomUUID(), "Peres", new CPF("12345678910"));

        assertEquals(c1, c2);
    }
}
