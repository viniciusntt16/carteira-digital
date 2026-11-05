package org.example.entidades;

import jakarta.persistence.*;
import org.example.objetos.TIPO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table (name = "transacoes")
public class Transacao {
    @Id
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TIPO tipo;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal valor;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conta_origem_numero", nullable = false)
    private Conta contaOrigem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conta_destino_numero")
    private Conta contaDestino;

    public Transacao() {
    }

    public Transacao(
            UUID id,
            TIPO tipo,
            BigDecimal valor,
            LocalDateTime dataHora,
            Conta contaOrigem
    ) {
        this(id, tipo, valor, dataHora, contaOrigem, null);
    }

    public Transacao(
            UUID id,
            TIPO tipo,
            BigDecimal valor,
            LocalDateTime dataHora,
            Conta contaOrigem,
            Conta contaDestino
    ) {
        this.id = Objects.requireNonNull(id, "Id não pode ser nulo");
        this.tipo = Objects.requireNonNull(tipo, "Tipo não pode ser nulo");
        this.dataHora = Objects.requireNonNull(
                dataHora, "Data não pode ser nula"
        );
        this.contaOrigem = Objects.requireNonNull(
                contaOrigem, "Conta de origem não pode ser nula"
        );

        this.valor = padronizarValor(valor);
        if (this.valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "O valor deve ser maior que zero"
            );
        }

        if (tipo == TIPO.TRANSFERENCIA && contaDestino == null) {
            throw new IllegalArgumentException(
                    "Uma transferência precisa de uma conta de destino"
            );
        }

        if (tipo != TIPO.TRANSFERENCIA && contaDestino != null) {
            throw new IllegalArgumentException(
                    "Somente transferências devem ter conta de destino"
            );
        }

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
