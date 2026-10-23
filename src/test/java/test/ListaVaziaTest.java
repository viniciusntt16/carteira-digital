package test;

import org.example.repositorios.ContaEmMemoria;
import org.example.repositorios.ContaRepository;
import org.example.entidades.Conta;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListaVaziaTest {

    @Test
    void listaVaziaConta(){
        ContaRepository contaRepository = new ContaEmMemoria();
        List<Conta> resultado = contaRepository.lista();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
