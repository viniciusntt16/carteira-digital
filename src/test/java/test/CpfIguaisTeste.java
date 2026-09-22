package test;

import org.example.objetos.CPF;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CpfIguaisTeste {

    @Test
    void deveConsiderarIguaisDoisCpfsComMesmoValor() {
        CPF primeiro = new CPF("12345678909");
        CPF segundo = new CPF("12345678909");

        assertEquals(primeiro, segundo);
    }
}
