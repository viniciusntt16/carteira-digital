package org.example.servicos;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.entidades.Transacao;
import org.example.exceptions.ContaNaoEncontradaException;
import org.example.objetos.TIPO;
import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoRepository;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class RelatorioService {
    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    public RelatorioService(ContaRepository contaRepository, TransacaoRepository transacaoRepository) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    public List<Transacao> extrato(int numeroConta) {
        return contaRepository.buscarPorId(numeroConta)
                .map(conta -> transacaoRepository.lista().stream()
                        .filter(transacao -> conta.equals(transacao.getContaOrigem())
                                        || conta.equals(transacao.getContaDestino())
                                ).sorted(
                                        Comparator.comparing(Transacao::getDataHora)
                                                .reversed()
                                ).toList())
                .orElseGet(List::of);
    }

    public Map<TIPO, BigDecimal> totalPorTipo(){
        return transacaoRepository.lista().stream()
                .collect(Collectors.groupingBy(Transacao::getTipo, Collectors.reducing(
                        BigDecimal.ZERO, Transacao::getValor, BigDecimal::add
                )));
    }

    public Optional<Cliente> clienteComMaiorSaldo(){
        return contaRepository.lista().stream().max(Comparator.comparing(
                Conta::getSaldo
        )).map(Conta::getCliente);
    }

    public List<Conta> contasSemMovimentacao(){
        List<Transacao> transacoes = transacaoRepository.lista();
        return contaRepository.lista()
                .stream()
                .filter(conta ->
                        transacoes.stream().noneMatch(transacao ->
                                conta.equals(transacao.getContaOrigem())
                                        || conta.equals(transacao.getContaDestino())))
                .toList();
    }

    public Map<Cliente, BigDecimal> saldoTotalPorCliente() {
        return contaRepository.lista()
                .stream()
                .collect(Collectors.groupingBy(Conta::getCliente,
                        Collectors.reducing(
                                BigDecimal.ZERO, Conta::getSaldo, BigDecimal::add
                                )
                        )
                );
    }
}
