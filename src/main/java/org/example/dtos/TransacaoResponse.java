package org.example.dtos;

import org.example.objetos.TIPO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record TransacaoResponse (

        UUID id,
        TIPO tipo,
        BigDecimal valor,
        LocalDateTime dataHora
){}

