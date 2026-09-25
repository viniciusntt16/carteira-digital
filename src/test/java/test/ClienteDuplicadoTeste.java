package test;

import org.example.entidades.Cliente;
import org.example.exceptions.ClienteDuplicadoException;
import org.example.objetos.CPF;
import org.example.repositorios.ClienteEmMemoria;
import org.example.repositorios.ClienteRepository;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClienteDuplicadoTeste {
    @Test
    void clienteDuplicado() {
        ClienteRepository repository = new ClienteEmMemoria();
        Cliente c1 = new Cliente(UUID.randomUUID(), "Vinicius", new CPF("12345678910"));
        Cliente c2 = new Cliente(UUID.randomUUID(), "Peres", new CPF("12345678910"));
        repository.salvar(c1);
        assertThrows(ClienteDuplicadoException.class,
                () -> repository.salvar(c2));
    }
    @Test
    void salvarDoisClientes(){
        ClienteRepository repository = new ClienteEmMemoria();
        Cliente c1 = new Cliente(UUID.randomUUID(), "Vinicius", new CPF("12345678910"));
        Cliente c2 = new Cliente(UUID.randomUUID(), "Peres", new CPF("12345678911"));

        repository.salvar(c1);
        repository.salvar(c2);
        assertEquals(2, repository.lista().size());
    }
}
