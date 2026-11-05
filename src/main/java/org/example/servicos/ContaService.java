package org.example.servicos;

import org.springframework.transaction.annotation.Transactional;
import org.example.entidades.Conta;
import org.example.entidades.Transacao;
import org.example.exceptions.ContaNaoEncontradaException;
import org.example.objetos.TIPO;
import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ContaService {
    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    public ContaService(ContaRepository contaRepository, TransacaoRepository transacaoRepository) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional
    public void depositar(int numeroConta, BigDecimal valor){

        Conta conta = contaRepository.findById(numeroConta)
                .orElseThrow(()->new ContaNaoEncontradaException(numeroConta));
        conta.creditar(valor);
        contaRepository.save(conta);
        Transacao transacao = new Transacao(UUID.randomUUID(), TIPO.DEPOSITO, valor,
                LocalDateTime.now(), conta);
        transacaoRepository.save(transacao);
    }

    @Transactional
    public void sacar(int numeroConta, BigDecimal valor){
        Conta conta = contaRepository.findById(numeroConta)
                .orElseThrow(()->new ContaNaoEncontradaException(numeroConta));
        conta.debitar(valor);
        contaRepository.save(conta);
        Transacao transacao = new Transacao(UUID.randomUUID(), TIPO.SAQUE, valor,
                LocalDateTime.now(), conta);
        transacaoRepository.save(transacao);
    }

    @Transactional
    public void transferir(int numeroOrigem, int numeroDestino, BigDecimal valor){
        if(numeroOrigem == numeroDestino){
            throw new IllegalArgumentException("A conta de origem e destino da transferencia devem ser diferentes");
        }
        Conta contaOrigem = contaRepository.findById(numeroOrigem)
                .orElseThrow(()-> new ContaNaoEncontradaException(numeroOrigem));
        Conta contaDestino = contaRepository.findById(numeroDestino)
                .orElseThrow(()-> new ContaNaoEncontradaException(numeroDestino));
        contaOrigem.debitar(valor);
        contaDestino.creditar(valor);
        contaRepository.save(contaOrigem);
        contaRepository.save(contaDestino);

        Transacao transacao = new Transacao(UUID.randomUUID(), TIPO.TRANSFERENCIA, valor,
                LocalDateTime.now(), contaOrigem, contaDestino);
        transacaoRepository.save(transacao);
    }
}
