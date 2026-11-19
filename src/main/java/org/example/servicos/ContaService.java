package org.example.servicos;

import org.example.entidades.Conta;
import org.example.entidades.Transacao;
import org.example.exceptions.ConflitoContaException;
import org.example.exceptions.ContaNaoEncontradaException;
import org.example.objetos.TIPO;
import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;
    private final Clock clock;

    public ContaService(
            ContaRepository contaRepository,
            TransacaoRepository transacaoRepository,
            Clock clock
    ) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
        this.clock = clock;
    }

    @Transactional
    public void depositar(int numeroConta, BigDecimal valor) {
        try {
            Conta conta = buscarConta(numeroConta);

            conta.creditar(valor);

            Transacao transacao = new Transacao(
                    UUID.randomUUID(),
                    TIPO.DEPOSITO,
                    valor,
                    LocalDateTime.now(clock),
                    conta
            );

            transacaoRepository.save(transacao);
            contaRepository.flush();

        } catch (OptimisticLockingFailureException e) {
            throw new ConflitoContaException(numeroConta);
        }
    }

    @Transactional
    public void sacar(int numeroConta, BigDecimal valor) {
        try {
            Conta conta = buscarConta(numeroConta);

            conta.debitar(valor);

            Transacao transacao = new Transacao(
                    UUID.randomUUID(),
                    TIPO.SAQUE,
                    valor,
                    LocalDateTime.now(clock),
                    conta
            );

            transacaoRepository.save(transacao);
            contaRepository.flush();

        } catch (OptimisticLockingFailureException e) {
            throw new ConflitoContaException(numeroConta);
        }
    }

    @Transactional
    public void transferir(
            int numeroOrigem,
            int numeroDestino,
            BigDecimal valor
    ) {
        if (numeroOrigem == numeroDestino) {
            throw new IllegalArgumentException(
                    "A conta de origem e destino da transferência devem ser diferentes"
            );
        }

        try {
            Conta contaOrigem = buscarConta(numeroOrigem);
            Conta contaDestino = buscarConta(numeroDestino);

            contaOrigem.debitar(valor);
            contaDestino.creditar(valor);

            Transacao transacao = new Transacao(
                    UUID.randomUUID(),
                    TIPO.TRANSFERENCIA,
                    valor,
                    LocalDateTime.now(clock),
                    contaOrigem,
                    contaDestino
            );

            transacaoRepository.save(transacao);
            contaRepository.flush();

        } catch (OptimisticLockingFailureException e) {
            throw new ConflitoContaException(
                    numeroOrigem,
                    numeroDestino
            );
        }
    }

    private Conta buscarConta(int numeroConta) {
        return contaRepository.findById(numeroConta)
                .orElseThrow(() -> new ContaNaoEncontradaException(numeroConta));
    }
}
