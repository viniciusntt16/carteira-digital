package org.example.entidades;

import org.example.objetos.TIPO;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transacao {
    private final UUID id;
    private final TIPO tipo;
    private final double valor;
    private final LocalDateTime dataHora;
    private final Conta contaOrigem;
    private Conta contaDestino;


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

    public UUID getId() {
        return id;
    }

    public TIPO getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public Conta getContaOrigem() {
        return contaOrigem;
    }

    public Conta getContaDestino() {
        return contaDestino;
    }

}
