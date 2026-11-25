package org.example.controller;

import org.example.dtos.ContaRequest;
import org.example.dtos.ContaResponse;
import org.example.dtos.TransacaoResponse;
import org.example.objetos.TIPO;
import org.example.servicos.ContaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContaController.class)
class ContaControllerTest {

    private static final String CPF_VALIDO = "52998224725";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ContaService contaService;

    @Test
    void deveCriarContaQuandoRequestForValido() throws Exception {
        UUID clienteId = UUID.randomUUID();

        ContaRequest request = new ContaRequest(
                1001,
                clienteId,
                new BigDecimal("1000.00")
        );

        ContaResponse response = new ContaResponse(
                1001,
                clienteId,
                new BigDecimal("1000.00")
        );

        when(contaService.criar(request)).thenReturn(response);

        String json = """
                {
                    "numero": 1001,
                    "clienteId": "%s",
                    "saldo": 1000.00
                }
                """.formatted(clienteId);

        mockMvc.perform(post("/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numero").value(1001))
                .andExpect(jsonPath("$.clienteId").value(clienteId.toString()))
                .andExpect(jsonPath("$.saldo").value(1000.00))
                .andExpect(jsonPath("$.cliente").doesNotExist())
                .andExpect(jsonPath("$.versao").doesNotExist());

        verify(contaService).criar(request);
    }

    @Test
    void deveBuscarContaPorNumero() throws Exception {
        UUID clienteId = UUID.randomUUID();
        ContaResponse response = new ContaResponse(1001,
                clienteId,
                BigDecimal.valueOf(1000.00));

        when(contaService.buscarPorNumero(1001)).thenReturn(response);

        mockMvc.perform(get("/contas/1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value(1001))
                .andExpect(jsonPath("$.clienteId").value(clienteId.toString()))
                .andExpect(jsonPath("$.saldo").value(1000.00))
                .andExpect(jsonPath("$.cliente").doesNotExist())
                .andExpect(jsonPath("$.versao").doesNotExist());

        verify(contaService).buscarPorNumero(1001);
    }

    @Test
    void deveRetornarExtratoDaConta() throws Exception {
        UUID transacaoId = UUID.randomUUID();
        TransacaoResponse transacaoResponse = new TransacaoResponse(
                transacaoId,
              TIPO.DEPOSITO,
              BigDecimal.valueOf(1000),
                LocalDateTime.of(2026, 1,1,1,0)
        );
        when(contaService.buscarTransacoesPorConta(1001))
                .thenReturn(List.of(transacaoResponse));

        mockMvc.perform(get("/contas/1001/extrato"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(transacaoId.toString()))
                .andExpect(jsonPath("$[0].tipo").value("DEPOSITO"))
                .andExpect(jsonPath("$[0].valor").value(1000.00))
                .andExpect(jsonPath("$[0].dataHora").exists())
                .andExpect(jsonPath("$[0].contaOrigem").doesNotExist())
                .andExpect(jsonPath("$[0].contaDestino").doesNotExist());

        verify(contaService).buscarTransacoesPorConta(1001);
    }

    @Test
    void deveRetornarListaVaziaQuandoContaNaoTiverTransacoes()
            throws Exception {
        when(contaService.buscarTransacoesPorConta(1001))
                .thenReturn(List.of());

        mockMvc.perform(get("/contas/1001/extrato"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());

        verify(contaService).buscarTransacoesPorConta(1001);
    }

    @Test
    void deveRejeitarSaldoComMaisDeDuasCasasDecimais()
            throws Exception {
        UUID clienteId = UUID.randomUUID();

        String json = """
                {
                    "numero": 1001,
                    "clienteId": "%s",
                    "saldo": 10.999
                }
                """.formatted(clienteId);

        mockMvc.perform(post("/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(contaService);
    }
}

