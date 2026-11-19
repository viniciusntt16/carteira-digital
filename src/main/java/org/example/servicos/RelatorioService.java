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
        contaRepository.findById(numeroConta)
                .orElseThrow(() -> new ContaNaoEncontradaException(numeroConta));

        return transacaoRepository.buscarPorContaOrdenadoPorData(numeroConta);
    }

    public Map<TIPO, BigDecimal> totalPorTipo(){
        return transacaoRepository.findAll().stream()
                .collect(Collectors.groupingBy(Transacao::getTipo, Collectors.reducing(
                        BigDecimal.ZERO, Transacao::getValor, BigDecimal::add
                )));
    }

    public Optional<Cliente> clienteComMaiorSaldo(){
        return contaRepository.findAll().stream().max(Comparator.comparing(
                Conta::getSaldo
        )).map(Conta::getCliente);
    }

    public List<Conta> contasSemMovimentacao(){
        return contaRepository.buscarContasSemMovimentacao();
    }

    public Map<Cliente, BigDecimal> saldoTotalPorCliente() {
        return contaRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Conta::getCliente,
                        Collectors.reducing(
                                BigDecimal.ZERO, Conta::getSaldo, BigDecimal::add
                                )
                        )
                );
    }
}
