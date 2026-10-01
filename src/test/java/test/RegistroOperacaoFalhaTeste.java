package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.exceptions.ContaNaoEncontradaException;
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

public class RegistroOperacaoFalhaTeste {
    @Test
    void saque(){
        ContaRepository contaRepository = new ContaEmMemoria();
        TransacaoRepository transacaoRepository = new TransacaoEmMemoria();
        ContaService contaService = new ContaService(contaRepository, transacaoRepository);
        Cliente cliente = new Cliente(UUID.randomUUID(), "Vinicius",
                new CPF("12345678910"));
        Conta conta = new Conta(1, cliente, 1000);
        contaRepository.salvar(conta);

        assertThrows(SaldoInsuficienteException.class,
                ()->contaService.sacar(1,1500));
        assertEquals(0, transacaoRepository.lista().size());
    }

    @Test
    void deposito(){
        ContaRepository contaRepository = new ContaEmMemoria();
        TransacaoRepository transacaoRepository = new TransacaoEmMemoria();
        ContaService contaService = new ContaService(contaRepository, transacaoRepository);
        Cliente cliente = new Cliente(UUID.randomUUID(), "Vinicius",
                new CPF("12345678910"));
        Conta conta = new Conta(1, cliente, 1000);
        contaRepository.salvar(conta);

        assertThrows(IllegalArgumentException.class,
                ()->contaService.depositar(1,0));
        assertEquals(0, transacaoRepository.lista().size());
    }

    @Test
    void transferencia(){
        ContaRepository contaRepository = new ContaEmMemoria();
        TransacaoRepository transacaoRepository = new TransacaoEmMemoria();
        ContaService contaService = new ContaService(contaRepository, transacaoRepository);
        Cliente cliente = new Cliente(UUID.randomUUID(), "Vinicius",
                new CPF("12345678910"));
        Conta conta = new Conta(1, cliente, 1000);
        contaRepository.salvar(conta);

        assertThrows(ContaNaoEncontradaException.class,
                ()->contaService.transferir(1, 2, 500));
        assertEquals(0, transacaoRepository.lista().size());
    }
}
