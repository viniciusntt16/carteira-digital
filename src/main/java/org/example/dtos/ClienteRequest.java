package org.example.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClienteRequest(

        @NotBlank(message = "O nome é obrigatório")
        @Size(max = 255, message = "O limite de caracteres para o nome é 255")
        String nome,

        @NotBlank(message = "O CPF é obrigatório")
        @Pattern(
                regexp = "\\d{11}",
                message = "O CPF deve conter exatamente 11 dígitos."
        )
        String cpf
) {
}

