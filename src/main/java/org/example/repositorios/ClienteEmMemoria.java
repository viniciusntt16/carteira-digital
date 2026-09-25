package org.example.repositorios;

import org.example.entidades.Cliente;
import org.example.exceptions.ClienteDuplicadoException;

import java.util.*;

public class ClienteEmMemoria implements ClienteRepository{
    private final Map<UUID, Cliente> clienteMap = new HashMap<>();

    @Override
    public Cliente salvar(Cliente cliente) {
        Objects.requireNonNull(cliente, "Cliente não pode ser nulo");

        boolean cpfExiste = clienteMap.values().stream()
                .anyMatch(cliente1 -> cliente1.getCpf().equals(cliente.getCpf()));
        if(cpfExiste == true){
            throw new ClienteDuplicadoException(
                     cliente.getCpf());
        }
        clienteMap.put(cliente.getId(), cliente);
        return cliente;
    }

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        Objects.requireNonNull(id, "Id não pode ser nulo");
        return Optional.ofNullable(clienteMap.get(id));
    }

    @Override
    public List<Cliente> lista() {
        return new ArrayList<>(clienteMap.values());
    }
}
