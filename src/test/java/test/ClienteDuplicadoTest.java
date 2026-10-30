package test;

import org.example.entidades.Cliente;
import org.example.exceptions.ClienteDuplicadoException;
import org.example.objetos.CPF;
import org.example.repositorios.ClienteRepository;
import org.example.servicos.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ClienteDuplicadoTest {
    ClienteRepository clienteRepository;
    ClienteService clienteService;
    Cliente c1;
    Cliente c2;
    Cliente c3;

    @BeforeEach
    void preparar(){
        clienteRepository = mock(ClienteRepository.class);
        clienteService = new ClienteService(clienteRepository);

        c1 = new Cliente(UUID.randomUUID(), "Vinicius", new CPF("12345678910"));
        c2 = new Cliente(UUID.randomUUID(), "Peres", new CPF("12345678910"));
        c3 = new Cliente(UUID.randomUUID(), "Peres", new CPF("12345678911"));

    }

    @Test
    void naoDeveSalvarClienteDuplicado(){
        when(clienteRepository.findByCpf(c2.getCpf())).thenReturn(Optional.of(c1));
        assertThrows(ClienteDuplicadoException.class, ()->clienteService.salvar(c2));
    }

    @Test
    void salvarDoisClientes(){
        when(clienteRepository.findByCpf(c1.getCpf())).thenReturn(Optional.of(c1));
        when(clienteRepository.findByCpf(c3.getCpf())).thenReturn(Optional.of(c3));
        when(clienteRepository.save(c1)).thenReturn(c1);
        when(clienteRepository.save(c3)).thenReturn(c3);
        clienteService.salvar(c1);
        clienteService.salvar(c3);
        verify(clienteRepository).save(c1);
        verify(clienteRepository).save(c3);
    }
}
