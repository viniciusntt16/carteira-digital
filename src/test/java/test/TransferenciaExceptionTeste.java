package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.exceptions.SaldoInsuficienteException;
import org.example.objetos.CPF;
import org.example.repositorios.ContaEmMemoria;
import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoEmMemoria;
import org.example.repositorios.TransacaoRepository;
import org.example.servicos.ContaService;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransferenciaExceptionTeste {
    @Test
    void transferencia(){
        ContaRepository contaRepository = new ContaEmMemoria();
        TransacaoRepository transacaoRepository = new TransacaoEmMemoria();
        ContaService contaService = new ContaService(contaRepository, transacaoRepository);
        Cliente c1 = new Cliente(UUID.randomUUID(), "Vinicius",
                new CPF("12345678910"));
        Cliente c2 = new Cliente(UUID.randomUUID(), "Peres",
                new CPF("12345678911"));
        Conta conta = new Conta(1, c1, 1000);
        Conta conta2 = new Conta(2, c2, 2000);
        contaRepository.salvar(conta);
        contaRepository.salvar(conta2);

        assertThrows(SaldoInsuficienteException.class,
                ()->contaService.transferir(2, 1, 2500));
        assertEquals(1000, conta.getSaldo());
        assertEquals(2000, conta2.getSaldo());
    }
}
