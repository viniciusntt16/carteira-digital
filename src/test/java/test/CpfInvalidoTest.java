package test;

import org.example.objetos.CPF;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CpfInvalidoTest {
    @Test
    void cpfInvalido(){
        assertThrows(IllegalArgumentException.class, () -> {
            new CPF("abababababa");
        });
    }
}
