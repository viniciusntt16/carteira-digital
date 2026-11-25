package org.example.controller;

import jakarta.validation.Valid;
import org.example.dtos.ClienteRequest;
import org.example.dtos.ClienteResponse;
import org.example.entidades.Cliente;
import org.example.servicos.ClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> criar(
            @Valid @RequestBody ClienteRequest request
    ) {
        Cliente salvo = clienteService.criar(request.nome(), request.cpf());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(paraResponse(salvo));
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return clienteService.lista()
                .stream()
                .map(this::paraResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(
            @PathVariable UUID id
    ) {
        return clienteService.buscarPorId(id)
                .map(cliente -> ResponseEntity.ok(paraResponse(cliente)))
                .orElse(ResponseEntity.notFound().build());
    }

    private ClienteResponse paraResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf().valor()
        );
    }

}
