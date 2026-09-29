package org.example.exceptions;

public class ContaNaoEncontradaException extends RuntimeException{
    public ContaNaoEncontradaException(int conta){
        super("Conta com o id " + conta + " não foi encontrada");
    }
}
