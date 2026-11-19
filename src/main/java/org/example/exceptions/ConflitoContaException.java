package org.example.exceptions;

public class ConflitoContaException extends RuntimeException {

    public ConflitoContaException(int numeroConta) {
        super("A conta " + numeroConta
                + " foi alterada por outra operação. Atualize os dados e tente novamente.");
    }
    public ConflitoContaException(int numeroOrigem, int numeroDestino) {
        super("Uma das contas da transferência (" + numeroOrigem + " ou "
                + numeroDestino + ") foi alterada por outra operação. "
                + "Tente novamente.");
    }
}
