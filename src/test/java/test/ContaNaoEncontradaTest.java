package test;

import org.example.entidades.Transacao;
import org.example.exceptions.ContaNaoEncontradaException;
import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoRepository;
import org.example.servicos.ContaService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ContaNaoEncontradaTest {
    @Test
    void contaNaoEncontrada(){
        ContaRepository contaRepository = mock(ContaRepository.class);
        TransacaoRepository transacaoRepository = mock(TransacaoRepository.class);
        ContaService contaService = new ContaService(contaRepository, transacaoRepository);

        when(contaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ContaNaoEncontradaException.class,
                ()-> contaService.depositar(1, BigDecimal.valueOf(500)));
        verify(contaRepository).findById(1);
        verify(transacaoRepository, never()).save(any(Transacao.class));
    }
}
