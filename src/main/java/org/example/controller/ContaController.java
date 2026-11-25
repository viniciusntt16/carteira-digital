package org.example.controller;

import jakarta.validation.Valid;
import org.example.dtos.ContaRequest;
import org.example.dtos.ContaResponse;
import org.example.dtos.TransacaoResponse;
import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.exceptions.ContaNaoEncontradaException;
import org.example.repositorios.ClienteRepository;
import org.example.repositorios.ContaRepository;
import org.example.servicos.ContaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/contas")
public class ContaController {
    private  final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<ContaResponse> criar(
            @Valid @RequestBody ContaRequest request
            ){
        ContaResponse response = contaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{numero}")
    public ContaResponse buscarPorNumero(@PathVariable int numero) {
        return contaService.buscarPorNumero(numero);
    }

    @GetMapping("/{numero}/extrato")
    public List<TransacaoResponse> buscarTransacoesPorNumero(@PathVariable int numero) {
        return contaService.buscarTransacoesPorConta(numero);
    }
}
