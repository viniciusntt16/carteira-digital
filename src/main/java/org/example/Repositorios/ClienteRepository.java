package org.example.Repositorios;

import org.example.entidades.Cliente;
import org.example.objetos.CPF;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository {
    Cliente salvarCliente(Cliente cliente);
    Optional<Cliente> buscarPorId(UUID id);
    List<Cliente> listaClientes();
}
