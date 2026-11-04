package test;

import org.example.exceptions.ContaNaoEncontradaException;

import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoEmMemoria;
import org.example.repositorios.TransacaoRepository;
import org.example.servicos.ContaService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ContaNaoEncontradaTest {
    @Test
    void contaNaoEncontrada(){
        ContaRepository contaRepository = mock(ContaRepository.class);
        TransacaoRepository transacaoRepository = new TransacaoEmMemoria();
        ContaService contaService = new ContaService(contaRepository,
                transacaoRepository);
        when(contaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ContaNaoEncontradaException.class,
                () -> contaService.sacar(1, BigDecimal.valueOf(500)));
    }
}
