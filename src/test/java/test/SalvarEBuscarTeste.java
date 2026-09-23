package test;

import org.example.Repositorios.ClienteEmMemoria;
import org.example.Repositorios.ClienteRepository;
import org.example.Repositorios.ContaEmMemoria;
import org.example.Repositorios.ContaRepository;
import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.objetos.CPF;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;

public class SalvarEBuscarTeste {
    @Test
    void SalvaEBuscaConta(){
        Cliente c1 = new Cliente(
                UUID.randomUUID(), "Vinicius", new CPF("12345678910"));
        Conta conta = new Conta(1, c1, 2000);
        ContaRepository contaRepository = new ContaEmMemoria();
        contaRepository.salvar(conta);
        Conta ResultadoBuscaConta = contaRepository.buscarPorId(1).orElseThrow();
        assertSame(conta, ResultadoBuscaConta);
    }

    @Test
    void SalvaEBuscaCliente(){
        UUID id = new UUID(0L, 1L);
        Cliente c1 = new Cliente(
                id, "Vinicius", new CPF("12345678910"));
        ClienteRepository clienteRepository = new ClienteEmMemoria();
        clienteRepository.salvarCliente(c1);
        Cliente ResultadoBuscaCliente = clienteRepository.buscarPorId(id).orElseThrow();
        assertSame(c1, ResultadoBuscaCliente);
    }
}
