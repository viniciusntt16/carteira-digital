package org.example.Repositorios;

import org.example.entidades.Cliente;

import java.util.*;

public class ClienteEmMemoria implements ClienteRepository{
    private final Map<UUID, Cliente> clienteMap = new HashMap<>();

    @Override
    public Cliente salvarCliente(Cliente cliente) {
        Objects.requireNonNull(cliente, "Cliente não pode ser nulo");
        clienteMap.put(cliente.getId(), cliente);
        return cliente;
    }

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        Objects.requireNonNull(id, "Id não pode ser nulo");
        return Optional.ofNullable(clienteMap.get(id));
    }

    @Override
    public List<Cliente> listaClientes() {
        return new ArrayList<>(clienteMap.values());
    }
}
