package org.example.entidades;

import jakarta.persistence.*;
import org.example.conversores.CPFConverter;
import org.example.objetos.CPF;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "Clientes")
public class Cliente {
    @Id
    protected UUID id;
    @Column(nullable = false)
    protected String nome;
    @Convert(converter = CPFConverter.class)
    @Column(nullable = false, unique = true, length = 11)
    protected CPF cpf;

    protected Cliente(){}

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
        this.nome = Objects.requireNonNull(nome, "O nome não pode ser null");
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
