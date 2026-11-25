package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.entidades.Transacao;
import org.example.exceptions.ConflitoContaException;
import org.example.objetos.CPF;
import org.example.repositorios.ClienteRepository;
import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoRepository;
import org.example.servicos.ContaService;
import org.example.servicos.RelatorioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.OptimisticLockingFailureException;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ContaServiceConflitoTest {
    private ContaRepository contaRepository;
    private TransacaoRepository transacaoRepository;
    private ClienteRepository clienteRepository;
    private RelatorioService relatorioService;
    private ContaService contaService;
    private Cliente cliente;
    private Conta conta;

    @BeforeEach
    void preparar() {
        contaRepository = mock(ContaRepository.class);
        transacaoRepository = mock(TransacaoRepository.class);
        clienteRepository = mock(ClienteRepository.class);
        relatorioService = mock(RelatorioService.class);

        contaService = new ContaService(
                contaRepository,
                transacaoRepository,
                clienteRepository,
                relatorioService,
                Clock.systemUTC()
        );

        cliente = new Cliente(
                UUID.randomUUID(),
                "Vinicius",
                new CPF("12345678910")
        );

        conta = new Conta(
                1,
                cliente,
                BigDecimal.valueOf(1000)
        );
    }

    @Test
    void conflitoAoDepositar(){
        when(contaRepository.findById(1)).thenReturn(Optional.of(conta));
        doThrow(new OptimisticLockingFailureException("Conflito simulado"))
                .when(contaRepository).flush();

        assertThrows(ConflitoContaException.class,
                ()->contaService.depositar(1, BigDecimal.valueOf(500)));
        verify(contaRepository).findById(1);
        verify(transacaoRepository).save(any(Transacao.class));
        verify(contaRepository).flush();
    }

    @Test
    void conflitoAoTransferir(){
        Conta contaDestino = new Conta(2, cliente, BigDecimal.valueOf(500));
        when(contaRepository.findById(1))
                .thenReturn(Optional.of(conta));

        when(contaRepository.findById(2))
                .thenReturn(Optional.of(contaDestino));

        doThrow(new OptimisticLockingFailureException("Conflito simulado"))
                .when(contaRepository).flush();

        assertThrows(ConflitoContaException.class,
                ()->contaService.transferir(1, 2,
                        BigDecimal.valueOf(500)));

        verify(contaRepository).findById(1);
        verify(contaRepository).findById(2);
        verify(transacaoRepository).save(any(Transacao.class));
        verify(contaRepository).flush();

    }
}
