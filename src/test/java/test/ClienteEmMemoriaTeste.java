package test;

import org.example.repositorios.ClienteEmMemoria;
import org.example.repositorios.ClienteRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClienteEmMemoriaTeste {
    @Test
    void clienteRetornaOptionalVazio(){
        ClienteRepository repository = new ClienteEmMemoria();
        Optional<?> resultado = repository.buscarPorId(UUID.randomUUID());
        assertTrue(resultado.isEmpty());
    }
}
