package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.objetos.CPF;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HashsetTest {
    @Test
    void naoAceitarOMesmoClientNohashset(){
        Set<Cliente> clientes = new HashSet<>();
        clientes.add(new Cliente(UUID.randomUUID(),
                "Vinicius", new CPF("12345678910")));
        clientes.add(new Cliente(UUID.randomUUID(),
                "Vinicius Aguiar", new CPF("12345678910")));

        int tamanhoEsperadoDoSet = clientes.size();
        assertEquals(1, tamanhoEsperadoDoSet);
    }
    @Test
    void naoAceitarAMesmoContaNohashset(){
        Cliente c1 = new Cliente(UUID.randomUUID(),"Vinicius",new CPF("12345678910"));
        Cliente c2 = new Cliente(UUID.randomUUID(), "Peres", new CPF("12345678910"));

        Set<Conta> contas = new HashSet<>();
        contas.add(new Conta(1, c1, 1000));
        contas.add(new Conta(1, c1, 1500));

        int tamanhoEsperadoDoSet = contas.size();
        assertEquals(1, tamanhoEsperadoDoSet);
    }
}
