package org.example.exceptions;

public class SaldoInsuficienteException extends RuntimeException{
    public SaldoInsuficienteException(int num, double valor, double saldo){
        super("A conta de numero " + num + " não pode realizar uma operação neste valor:"
        + valor + ". O saldo é " + saldo + ", digite um valor igual ou menor que esse.");
    }
}
