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

}
