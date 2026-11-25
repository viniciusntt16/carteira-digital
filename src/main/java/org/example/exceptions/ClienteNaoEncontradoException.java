package org.example.exceptions;

import java.util.UUID;

public class ClienteNaoEncontradoException extends RuntimeException {

    public ClienteNaoEncontradoException(UUID id) {
        super("Cliente com o id " + id + " não foi encontrado.");
    }
}
