package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.objetos.CPF;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DuasCasasDecimaisTest {
    @Test
    void duasCasasDecimais(){
        Cliente cliente = new Cliente(UUID.randomUUID(), "Vinicius",
                new CPF("12345678910"));
        Conta conta = new Conta(1, cliente, BigDecimal.valueOf(500));
        assertThrows(IllegalArgumentException.class,
                ()->conta.debitar(BigDecimal.valueOf(20.505)));
        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_EVEN),
                conta.getSaldo());
    }
}
