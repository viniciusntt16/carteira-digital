package org.example.servicos;

import org.example.entidades.Conta;
import org.example.entidades.Transacao;
import org.example.exceptions.ContaNaoEncontradaException;
import org.example.objetos.TIPO;
import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public class ContaService {
    ContaRepository contaRepository;
    TransacaoRepository transacaoRepository;

    public ContaService(ContaRepository contaRepository, TransacaoRepository transacaoRepository) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    public void depositar(int numeroConta, double valor){
        if(valor <= 0){
            throw new IllegalArgumentException();
        }
        Conta conta = contaRepository.buscarPorId(numeroConta)
                .orElseThrow(()->new ContaNaoEncontradaException(numeroConta));
        conta.creditar(valor);

        Transacao transacao = new Transacao(UUID.randomUUID(), TIPO.DEPOSITO, valor,
                LocalDateTime.now(), conta);
        transacaoRepository.salvar(transacao);

    }

    public void sacar(int numeroConta, double valor){
        if(valor <= 0){
            throw new IllegalArgumentException();
        }
        Conta conta = contaRepository.buscarPorId(numeroConta)
                .orElseThrow(()->new ContaNaoEncontradaException(numeroConta));
        conta.debitar(valor);

        Transacao transacao = new Transacao(UUID.randomUUID(), TIPO.SAQUE, valor,
                LocalDateTime.now(), conta);
        transacaoRepository.salvar(transacao);
    }

    public void transferir(int numeroOrigem, int numeroDestino, double valor){
        if(numeroOrigem == numeroDestino){
            throw new IllegalArgumentException();
        }
        if(valor <= 0){
            throw new IllegalArgumentException();
        }
        Conta contaOrigem = contaRepository.buscarPorId(numeroOrigem)
                .orElseThrow(()-> new ContaNaoEncontradaException(numeroOrigem));
        Conta contaDestino = contaRepository.buscarPorId(numeroDestino)
                .orElseThrow(()-> new ContaNaoEncontradaException(numeroDestino));
        contaOrigem.debitar(valor);
        contaDestino.creditar(valor);

        Transacao transacao = new Transacao(UUID.randomUUID(), TIPO.TRANSFERENCIA, valor,
                LocalDateTime.now(), contaOrigem, contaDestino);
        transacaoRepository.salvar(transacao);
    }
}
