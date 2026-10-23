package test;

import org.example.entidades.Cliente;
import org.example.repositorios.ClienteRepository;
import org.example.servicos.ClienteService;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ClienteServiceTest {
    @Test
    void clienteRetornaOptionalVazio(){
        ClienteRepository repository = mock(ClienteRepository.class);
        ClienteService clienteService = new ClienteService(repository);
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Cliente> resultado = clienteService.buscarPorId(id);
        assertTrue(resultado.isEmpty());
        verify(repository).findById(id);
    }
}
