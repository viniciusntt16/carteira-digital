package org.example.entidades;

import org.example.objetos.CPF;

import java.util.UUID;

public class Cliente {
    protected UUID id;
    protected String nome;
    protected final CPF cpf;

    public Cliente(UUID id, String nome, CPF cpf) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public CPF getCpf() {
        return cpf;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Cliente outro)) return false;
        return cpf.equals(outro.cpf);
    }

    @Override
    public int hashCode() {
        return cpf.hashCode();
    }



}
