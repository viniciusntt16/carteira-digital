package org.example;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.entidades.Transacao;
import org.example.objetos.CPF;
import org.example.objetos.TIPO;

import java.time.LocalDateTime;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        Cliente c1 = new Cliente(
                UUID.randomUUID(), "Vinicius", new CPF("12345678910"));
        Cliente c2 = new Cliente(
                UUID.randomUUID(), "Peres", new CPF("12345678901")
        );
        Conta conta1 = new Conta(1, c1, 2000);
        Conta conta2 = new Conta(2, c2, 5000);
        Transacao t1 = new Transacao(UUID.randomUUID(),
                TIPO.SAQUE,
                500,
                LocalDateTime.now(),
                conta1);
        t1.executar();
        System.out.println("Saldo apos saque da conta1 = " + conta1.getSaldo());

        Transacao t2 = new Transacao(UUID.randomUUID(),
                TIPO.TRANSFERENCIA,
                1500,
                LocalDateTime.now(),
                conta2, conta1);
        t2.executar();
        System.out.println("Saldo final da conta1 = " + conta1.getSaldo());
        System.out.println("Saldo final da conta2 = " + conta2.getSaldo());
        System.out.println(conta1.getTransacoes());
    }
}