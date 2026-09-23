package test;

import org.example.Repositorios.ContaEmMemoria;
import org.example.Repositorios.ContaRepository;
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
