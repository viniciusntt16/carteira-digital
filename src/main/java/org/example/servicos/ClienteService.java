package org.example.servicos;

import org.example.entidades.Cliente;
import org.example.exceptions.ClienteDuplicadoException;
import org.example.objetos.CPF;
import org.example.repositorios.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClienteService {
    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente salvar (Cliente cliente){
        clienteRepository.findByCpf(cliente.getCpf()).filter(
                clienteExistente -> !clienteExistente.getId().equals(cliente.getId())
        ).ifPresent(cliente1 -> {throw new ClienteDuplicadoException(cliente.getCpf());
        });
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> buscarPorId(UUID id){
        return clienteRepository.findById(id);
    }

    public List<Cliente> lista(){
        return clienteRepository.findAll();
    }

    public Cliente criar(String nome, String cpf) {
        Cliente cliente = new Cliente(
                UUID.randomUUID(),
                nome,
                new CPF(cpf)
        );
        return salvar(cliente);
    }

}
