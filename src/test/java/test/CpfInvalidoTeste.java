package test;

import org.example.objetos.CPF;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CpfInvalidoTeste {
    @Test
    void cpfInvalido(){
        assertThrows(IllegalArgumentException.class, () -> {
            new CPF("1234567891");
        });
    }
}
