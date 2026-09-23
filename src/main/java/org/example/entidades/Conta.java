package org.example.entidades;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Conta {
    protected int numero;
    protected Cliente cliente;
    protected double saldo;
    private final List<String> transacoes = new ArrayList<>();

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

    public double getSaldo() {
        return saldo;
    }

    public void registrarTransacao(String registro) {
        transacoes.add(registro);
    }

    public List<String> getTransacoes() {
        return Collections.unmodifiableList(transacoes);
    }

    public int getNumero() {
        return numero;
    }
}
