package org.example.entidades;

import org.example.exceptions.SaldoInsuficienteException;

public class Conta {
    protected int numero;
    protected Cliente cliente;
    protected double saldo;

    public Conta(){}
    public Conta(int numero, Cliente cliente, double saldo) {
        this.numero = numero;
        this.cliente = cliente;
        this.saldo = saldo;
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
    public void creditar(double valor){
        saldo += valor;
    }
    public void debitar(double valor){
        if(saldo < valor){
            throw new SaldoInsuficienteException(numero, valor, saldo);
        }
        saldo -= valor;
    }
    public double getSaldo() {
        return saldo;
    }
    public int getNumero() {
        return numero;
    }
}
