package org.example.entidades;

import org.example.objetos.TIPO;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class Transacao {
    UUID id;
    TIPO tipo;
    double valor;
    LocalDateTime dataHora;
    Conta contaOrigem;
    Conta contaDestino;


    public Transacao(UUID id, TIPO tipo, double valor, LocalDateTime dataHora, Conta contaOrigem) {
        this.id = id;
        this.tipo = tipo;
        this.valor = valor;
        this.dataHora = dataHora;
        this.contaOrigem = contaOrigem;
    }

    public Transacao(UUID id, TIPO tipo, double valor, LocalDateTime dataHora, Conta contaOrigem, Conta contaDestino) {
        this.id = id;
        this.tipo = tipo;
        this.valor = valor;
        this.dataHora = dataHora;
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
    }

    public void saque(){
        if(valor > contaOrigem.saldo){
            throw new IllegalArgumentException(
                    "O valor do saque não pode ser maior que o saldo");
        }
        contaOrigem.registrarTransacao("Saque no valor de " + valor + " realizado\n");
    }

    public void transferencia(){
        if(valor <= 0){
            throw new IllegalArgumentException(
                    "O valor da transferência deve ser maior que 0"
            );
        }
        if(valor > contaOrigem.saldo){
            throw new IllegalArgumentException(
                    "O valor da transferência deve ser menor que o saldo"
            );
        }
        contaOrigem.registrarTransacao("Valor de " + valor + " enviado\n");
        contaDestino.registrarTransacao("Valor de " + valor + " recebido\n");
    }

    public void deposito(){
        contaOrigem.registrarTransacao("Deposito de " + valor + "realizado\n");
    }
}
