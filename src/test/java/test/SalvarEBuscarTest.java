package test;

import org.example.repositorios.ClienteRepository;
import org.example.repositorios.ContaEmMemoria;
import org.example.repositorios.ContaRepository;
import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.objetos.CPF;
import org.example.servicos.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

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
        ClienteRepository clienteRepository = mock(ClienteRepository.class);
        ClienteService clienteService = new ClienteService(clienteRepository);

        UUID id = UUID.randomUUID();

        Cliente cliente = new Cliente(
                id,
                "Vinicius",
                new CPF("12345678910")
        );

        when(clienteRepository.findById(id))
                .thenReturn(Optional.of(cliente));

        Cliente resultado = clienteService.buscarPorId(id).orElseThrow();

        assertEquals(cliente.getId(), resultado.getId());
        assertEquals(cliente.getNome(), resultado.getNome());
        assertEquals(cliente.getCpf(), resultado.getCpf());

        verify(clienteRepository).findById(id);
    }
}
