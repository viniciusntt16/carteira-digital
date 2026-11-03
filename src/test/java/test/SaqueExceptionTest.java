package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.exceptions.SaldoInsuficienteException;
import org.example.objetos.CPF;
import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoEmMemoria;
import org.example.repositorios.TransacaoRepository;
import org.example.servicos.ContaService;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SaqueExceptionTest {
    @Test
    void saqueException(){
        ContaRepository contaRepository = mock(ContaRepository.class);
        TransacaoRepository transacaoRepository = new TransacaoEmMemoria();
        ContaService contaService = new ContaService(contaRepository, transacaoRepository);
        Cliente cliente = new Cliente(UUID.randomUUID(), "Vinicius",
                new CPF("12345678910"));
        Conta conta = new Conta(1, cliente, BigDecimal.valueOf(1000));
        when(contaRepository.findById(1)).thenReturn(Optional.of(conta));

        assertThrows(SaldoInsuficienteException.class,
                ()->contaService.sacar(1,BigDecimal.valueOf(1500)));
        assertEquals(BigDecimal.valueOf(1000)
                .setScale(2, RoundingMode.HALF_EVEN), conta.getSaldo());
    }
}
