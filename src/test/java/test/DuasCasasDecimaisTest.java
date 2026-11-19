package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.entidades.Transacao;
import org.example.objetos.CPF;
import org.example.objetos.TIPO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DuasCasasDecimaisTest {
    private Cliente cliente;
    private Conta conta;

    @BeforeEach
    void preparar(){
        cliente = new Cliente(UUID.randomUUID(), "Vinicius",
                new CPF("12345678910"));
        conta = new Conta(1, cliente, BigDecimal.valueOf(500));
    }

    @Test
    void duasCasasDecimaisdebito(){
        assertThrows(IllegalArgumentException.class,
                ()->conta.debitar(BigDecimal.valueOf(20.505)));
        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_EVEN),
                conta.getSaldo());
    }

    @Test
    void duasCasasDecimaiscredito(){
        assertThrows(IllegalArgumentException.class,
                ()->conta.creditar(BigDecimal.valueOf(20.505)));
        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_EVEN),
                conta.getSaldo());
    }

    @Test
    void duasCasasDecimaisTransacao() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Transacao(UUID.randomUUID(), TIPO.DEPOSITO,
                        BigDecimal.valueOf(10.505),
                        LocalDateTime.of(2026, 1, 10, 10, 0),
                        conta
                )
        );
        assertEquals(BigDecimal.valueOf(500).setScale(2, RoundingMode.HALF_EVEN),
                conta.getSaldo());
    }
}
