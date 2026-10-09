package test;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ProblemasComTiposTest {

    @Test
    void problemaDouble(){
        double sum = 0.1 + 0.2;
        assertNotEquals(0.3, sum);
    }

    @Test
    void problemaBigDecimal(){
        BigDecimal b1 = new BigDecimal("1.0");
        BigDecimal b2 = new BigDecimal("1.00");
        assertNotEquals(b1, b2);
        assertEquals(0, b1.compareTo(b2));
    }

    @Test
    void diferencaBigDecimal(){
        BigDecimal b1 = new BigDecimal(0.1);
        BigDecimal b2 = new BigDecimal("0.1");
        assertNotEquals(b1, b2);
    }

}
