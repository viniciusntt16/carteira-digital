package org.example.exceptions;

public class ContaDuplicadaException extends RuntimeException {
    public ContaDuplicadaException(int numero) {
        super("Já existe uma conta com o número " + numero);
    }
}