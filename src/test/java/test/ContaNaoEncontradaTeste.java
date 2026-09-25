package test;

import org.example.exceptions.ContaNaoEncontradaException;
import org.example.repositorios.ContaEmMemoria;
import org.example.repositorios.ContaRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ContaNaoEncontradaTeste {
    @Test
    void contaNaoEncontrada(){
        ContaRepository contaRepository = new ContaEmMemoria();

        assertThrows(ContaNaoEncontradaException.class,
                () -> contaRepository.buscarPorId(1)
                        .orElseThrow(() -> new ContaNaoEncontradaException(
                                1)));
    }
}
