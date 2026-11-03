package test;


import org.example.repositorios.ContaRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class ContaEmMemoriaTest {
    @Test
    void contaRetornaOptionalRetornaVazio(){
        ContaRepository contaRepository = mock(ContaRepository.class);
        Optional<?> resultado = contaRepository.findById(5);
        assertTrue(resultado.isEmpty());
    }
}
