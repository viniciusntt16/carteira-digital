package org.example.entidades;

import org.example.objetos.TIPO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Transacao {
    private final UUID id;
    private final TIPO tipo;
    private final BigDecimal valor;
    private final LocalDateTime dataHora;
    private final Conta contaOrigem;
    private Conta contaDestino;


    public Transacao(UUID id, TIPO tipo, BigDecimal valor, LocalDateTime dataHora, Conta contaOrigem) {
        this.id = id;
        this.tipo = tipo;
        this.valor = padronizarValor(valor);
        this.dataHora = dataHora;
        this.contaOrigem = contaOrigem;
    }

    public Transacao(UUID id, TIPO tipo, BigDecimal valor, LocalDateTime dataHora, Conta contaOrigem, Conta contaDestino) {
        this.id = id;
        this.tipo = tipo;
        this.valor = padronizarValor(valor);
        this.dataHora = dataHora;
        this.contaOrigem = contaOrigem;
        this.contaDestino = contaDestino;
    }

    private BigDecimal padronizarValor(BigDecimal valor) {
        Objects.requireNonNull(valor, "Valor não pode ser nulo");

        return valor.setScale(2, RoundingMode.HALF_EVEN);
    }

    public UUID getId() {
        return id;
    }

    public TIPO getTipo() {
        return tipo;
    }

    public BigDecimal getValor() {
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
