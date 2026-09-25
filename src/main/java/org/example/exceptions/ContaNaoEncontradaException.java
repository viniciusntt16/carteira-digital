package org.example.exceptions;

import org.example.entidades.Conta;

public class ContaNaoEncontradaException extends RuntimeException{
    public ContaNaoEncontradaException(int conta){
        super("Conta com o id " + conta + " não foi encontrada");
    }
}
