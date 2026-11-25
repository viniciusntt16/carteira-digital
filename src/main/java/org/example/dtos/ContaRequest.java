package org.example.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.UUID;

public record ContaRequest(
        @Positive(message = "O numero da conta deve ser positivo")
        int numero,

        @NotNull(message = "O id do cliente não pode ser null")
        UUID clienteId,

        @NotNull(message = "O saldo inicial é obrigatório")
        @DecimalMin(value = "0.00", message = "O saldo não pode ser negativo")
        @Digits(integer = 17,
        fraction = 2,
        message = "O saldo deve ter até 17 digitos e 2 casas decimais")
        BigDecimal saldo
) {
}
