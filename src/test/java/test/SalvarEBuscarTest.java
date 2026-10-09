package test;

import org.example.repositorios.ClienteEmMemoria;
import org.example.repositorios.ClienteRepository;
import org.example.repositorios.ContaEmMemoria;
import org.example.repositorios.ContaRepository;
import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.objetos.CPF;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertSame;

public class SalvarEBuscarTest {
    private Cliente c1;
    private Conta conta;
    private ContaRepository contaRepository;

    @BeforeEach
    void preparar(){
        c1 = new Cliente(
                UUID.randomUUID(), "Vinicius", new CPF("12345678910"));
        conta = new Conta(1, c1, BigDecimal.valueOf(2000));
        contaRepository = new ContaEmMemoria();
        contaRepository.salvar(conta);
    }
    @Test
    void salvaEBuscaConta(){
        Conta resultadoBuscaConta = contaRepository.buscarPorId(1).orElseThrow();
        assertSame(conta, resultadoBuscaConta);
    }

    @Test
    void salvaEBuscaCliente(){
        UUID id = new UUID(0L, 1L);
        Cliente c2 = new Cliente(
                id, "Vinicius", new CPF("12345678910"));
        ClienteRepository clienteRepository = new ClienteEmMemoria();
        clienteRepository.salvar(c2);
        Cliente resultadoBuscaCliente = clienteRepository.buscarPorId(id).orElseThrow();
        assertSame(c2, resultadoBuscaCliente);
    }
}
