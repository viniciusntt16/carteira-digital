package test;

import org.example.repositorios.ClienteEmMemoria;
import org.example.repositorios.ClienteRepository;
import org.example.repositorios.ContaEmMemoria;
import org.example.repositorios.ContaRepository;
import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.objetos.CPF;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;

public class SalvarEBuscarTeste {
    @Test
    void salvaEBuscaConta(){
        Cliente c1 = new Cliente(
                UUID.randomUUID(), "Vinicius", new CPF("12345678910"));
        Conta conta = new Conta(1, c1, 2000);
        ContaRepository contaRepository = new ContaEmMemoria();
        contaRepository.salvar(conta);
        Conta resultadoBuscaConta = contaRepository.buscarPorId(1).orElseThrow();
        assertSame(conta, resultadoBuscaConta);
    }

    @Test
    void salvaEBuscaCliente(){
        UUID id = new UUID(0L, 1L);
        Cliente c1 = new Cliente(
                id, "Vinicius", new CPF("12345678910"));
        ClienteRepository clienteRepository = new ClienteEmMemoria();
        clienteRepository.salvar(c1);
        Cliente resultadoBuscaCliente = clienteRepository.buscarPorId(id).orElseThrow();
        assertSame(c1, resultadoBuscaCliente);
    }
}
