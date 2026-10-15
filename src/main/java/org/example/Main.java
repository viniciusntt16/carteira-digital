package org.example;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.entidades.Transacao;
import org.example.objetos.CPF;
import org.example.repositorios.ContaEmMemoria;
import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoEmMemoria;
import org.example.repositorios.TransacaoRepository;
import org.example.servicos.ContaService;
import org.example.servicos.RelatorioService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        ContaRepository contaRepository = new ContaEmMemoria();
        TransacaoRepository transacaoRepository = new TransacaoEmMemoria();
        ContaService contaService = new ContaService(contaRepository, transacaoRepository);

        Cliente c1 = new Cliente(UUID.randomUUID(), "Vinicius",
                new CPF("12345678910"));
        Cliente c2 = new Cliente(UUID.randomUUID(), "Peres",
                new CPF("12345678911"));
        Conta conta = new Conta(1, c1, BigDecimal.valueOf(5000));
        Conta conta2 = new Conta(2, c1, BigDecimal.valueOf(2000));
        Conta conta3 = new Conta(3, c2, BigDecimal.valueOf(7000));

        contaRepository.salvar(conta);
        contaRepository.salvar(conta2);
        contaRepository.salvar(conta3);

        RelatorioService relatorioService = new RelatorioService(
                contaRepository, transacaoRepository
        );

        contaService.sacar(1,BigDecimal.valueOf(1000));
        contaService.depositar(1, BigDecimal.valueOf(1000));
        contaService.sacar(2,BigDecimal.valueOf(500));
        contaService.depositar(2, BigDecimal.valueOf(500));
        contaService.sacar(3,BigDecimal.valueOf(2000));
        contaService.depositar(3, BigDecimal.valueOf(2000));
        contaService.transferir(3,1, BigDecimal.valueOf(2000));

        List<Transacao> extrato = relatorioService.extrato(1);

        System.out.println("Extrato da conta de numero 1:\n");
        extrato.forEach(transacao -> {
            String contaOrigem = transacao.getContaOrigem() == null ? "-"
                    : String.valueOf(transacao.getContaOrigem().getNumero());
            String contaDestino = transacao.getContaDestino() == null ? "-"
                    : String.valueOf(transacao.getContaDestino().getNumero());
            System.out.printf(
                    "Data: %s | Tipo: %s | Valor: R$ %s | Origem: %s | Destino: %s%n",
                    transacao.getDataHora(),
                    transacao.getTipo(),
                    transacao.getValor(),
                    contaOrigem,
                    contaDestino
            );
        });
    }
}