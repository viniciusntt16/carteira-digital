package org.example.dtos;

import java.math.BigDecimal;
import java.util.UUID;

public record ContaResponse(
        int numero,
        UUID clienteId,
        BigDecimal saldo
) {
}
