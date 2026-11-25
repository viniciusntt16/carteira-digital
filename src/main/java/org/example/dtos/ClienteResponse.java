package org.example.dtos;

import java.util.UUID;

public record ClienteResponse (
        UUID id,
        String nome,
        String cpf
){
}
