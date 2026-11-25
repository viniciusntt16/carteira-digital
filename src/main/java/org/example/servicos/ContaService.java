package org.example.servicos;

import org.example.dtos.ContaRequest;
import org.example.dtos.ContaResponse;
import org.example.dtos.TransacaoResponse;
import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.entidades.Transacao;
import org.example.exceptions.ClienteNaoEncontradoException;
import org.example.exceptions.ConflitoContaException;
import org.example.exceptions.ContaDuplicadaException;
import org.example.exceptions.ContaNaoEncontradaException;
import org.example.objetos.TIPO;
import org.example.repositorios.ClienteRepository;
import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoRepository;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;
    private final Clock clock;
    private final ClienteRepository clienteRepository;
    private final RelatorioService relatorioService;

    public ContaService(
            ContaRepository contaRepository,
            TransacaoRepository transacaoRepository,
            ClienteRepository clienteRepository,
            RelatorioService relatorioService,
            Clock clock
    ) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
        this.clienteRepository = clienteRepository;
        this.relatorioService = relatorioService;
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

    @Transactional
    public ContaResponse criar(ContaRequest request) {
        if(contaRepository.existsById(request.numero())){
            throw new ContaDuplicadaException(request.numero());
        }
        Cliente cliente = clienteRepository.findById(request.clienteId())
                .orElseThrow(()-> new ClienteNaoEncontradoException(request.clienteId()));

        Conta conta = new Conta(request.numero(),
                cliente, request.saldo());

        Conta contaSalva = contaRepository.save(conta);
        return paraContaResponse(contaSalva);
    }

    @Transactional(readOnly = true)
    public ContaResponse buscarPorNumero(int numero) {
        Conta conta = buscarConta(numero);
        return paraContaResponse(conta);
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponse> buscarTransacoesPorConta(int numero) {
        return relatorioService.extrato(numero).stream()
                .map(this::paraTransacaoResponse).toList();
    }

    private Conta buscarConta(int numeroConta) {
        return contaRepository.findById(numeroConta)
                .orElseThrow(() -> new ContaNaoEncontradaException(numeroConta));
    }

    private ContaResponse paraContaResponse(Conta conta) {
        return new ContaResponse(
                conta.getNumero(),
                conta.getCliente().getId(),
                conta.getSaldo()
        );
    }

    private TransacaoResponse paraTransacaoResponse(Transacao transacao) {
        return new TransacaoResponse(
                transacao.getId(),
                transacao.getTipo(),
                transacao.getValor(),
                transacao.getDataHora()
        );
    }
}
