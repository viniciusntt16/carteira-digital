package org.example.exceptions;

import org.example.objetos.CPF;

public class ClienteDuplicadoException extends RuntimeException {

    public ClienteDuplicadoException(CPF cpf){
        super("Já existe um cliente com o cpf " + cpf.valor() + " cadastrado");
    }
}
