package org.example.repositorios;

import org.example.entidades.Cliente;
import org.example.objetos.CPF;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest(showSql = false)
class ClienteRepositoryJpaTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void deveSubirContextoEInjetarRepositorio() {
        assertNotNull(clienteRepository);
    }

    @Test
    void deveSalvarEBuscarCliente() {
        UUID id = UUID.randomUUID();

        Cliente cliente = new Cliente(
                id,
                "Vinicius",
                new CPF("12345678910")
        );

        clienteRepository.saveAndFlush(cliente);

        Cliente clienteEncontrado = clienteRepository.findById(id)
                .orElseThrow();

        assertEquals(cliente.getId(), clienteEncontrado.getId());
        assertEquals(cliente.getNome(), clienteEncontrado.getNome());
        assertEquals(cliente.getCpf(), clienteEncontrado.getCpf());
    }
}

