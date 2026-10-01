package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.exceptions.ContaNaoEncontradaException;
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

public class TransferenciaContaNaoEncontradaTeste {
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
        assertEquals(1000, conta.getSaldo());
    }
}
