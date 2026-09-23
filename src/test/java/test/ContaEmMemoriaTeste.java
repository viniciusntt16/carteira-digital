package test;

import org.example.repositorios.ContaEmMemoria;
import org.example.repositorios.ContaRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContaEmMemoriaTeste {
    @Test
    void contaRetornaOptionalRetornaVazio(){
        ContaRepository contaRepository = new ContaEmMemoria();
        Optional<?> resultado = contaRepository.buscarPorId(5);
        assertTrue(resultado.isEmpty());
    }
}
