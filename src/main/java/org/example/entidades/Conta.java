package org.example.entidades;

import org.example.exceptions.SaldoInsuficienteException;

import java.math.BigDecimal;
import java.util.Objects;
import java.math.RoundingMode;

public class Conta {
    protected int numero;
    protected Cliente cliente;
    protected BigDecimal saldo;

    public Conta(){}
    public Conta(int numero, Cliente cliente, BigDecimal saldo) {
        this.numero = numero;
        this.cliente = cliente;
        this.saldo = padronizarValor(saldo);
    }
    @Override
    public boolean equals(Object obj){
        if(this == obj) return true;
        if(!(obj instanceof Conta outra)) return false;
        return numero == outra.numero;
    }
    @Override
    public int hashCode(){
        return Integer.hashCode(numero);
    }
    public void creditar(BigDecimal valor) {
        BigDecimal valorPadronizado = padronizarValor(valor);

        if (valorPadronizado.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "O valor do crédito deve ser maior que zero"
            );
        }

        saldo = saldo.add(valorPadronizado)
                .setScale(2, RoundingMode.HALF_EVEN);
    }
    public void debitar(BigDecimal valor){
        BigDecimal valorPadronizado = padronizarValor(valor);
        if(valorPadronizado.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("O valor a ser debitado deve ser maior que zero");
        }

        if(saldo.compareTo(valorPadronizado) < 0){
            throw new SaldoInsuficienteException(numero, valor, saldo);
        }
        saldo = saldo.subtract(valorPadronizado).setScale(2, RoundingMode.HALF_EVEN);
    }

    private BigDecimal padronizarValor(BigDecimal valor) {
        Objects.requireNonNull(valor, "Valor não pode ser nulo");

        return valor.setScale(2, RoundingMode.HALF_EVEN);
    }

    public BigDecimal getSaldo() {
        return saldo;
    }
    public int getNumero() {
        return numero;
    }

    public Cliente getCliente() {
        return cliente;
    }
}
