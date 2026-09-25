package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.objetos.CPF;
import org.junit.jupiter.api.Test;

import java.util.UUID;

public class SaldoInsuficienteTeste {
    @Test
    void saldoInsuficiente(){
        Cliente c1 = new Cliente(UUID.randomUUID(),
                "Vinicius", new CPF("12345678910"));
        Conta conta = new Conta(1, c1, 2000);

    }
}
