package org.example.repositorios;

import org.example.controller.ClienteController;
import org.example.entidades.Cliente;
import org.example.objetos.CPF;
import org.example.servicos.ClienteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    private static final String CPF_VALIDO = "52998224725";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @Test
    void deveCriarClienteQuandoRequestForValido() throws Exception {
        UUID id = UUID.randomUUID();

        Cliente clienteSalvo = new Cliente(
                id,
                "Vinicius",
                new CPF(CPF_VALIDO)
        );

        when(clienteService.criar("Vinicius", CPF_VALIDO))
                .thenReturn(clienteSalvo);

        String json = """
                {
                    "nome": "Vinicius",
                    "cpf": "52998224725"
                }
                """;

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nome").value("Vinicius"))
                .andExpect(jsonPath("$.cpf").value(CPF_VALIDO));

        verify(clienteService).criar("Vinicius", CPF_VALIDO);
    }

    @Test
    void deveRetornar400QuandoCpfNaoPossuir11Digitos() throws Exception {
        String json = """
                {
                    "nome": "Vinicius",
                    "cpf": "1234567890"
                }
                """;

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(clienteService);
    }

    @Test
    void deveRetornar400QuandoNomeEstiverEmBranco() throws Exception {
        String json = """
                {
                    "nome": "   ",
                    "cpf": "52998224725"
                }
                """;

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(clienteService);
    }

    @Test
    void deveRetornar400QuandoNomeExceder255Caracteres() throws Exception {
        String json = """
                {
                    "nome": "%s",
                    "cpf": "52998224725"
                }
                """.formatted("A".repeat(256));

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(clienteService);
    }

    @Test
    void deveListarClientes() throws Exception {
        Cliente clienteUm = new Cliente(
                UUID.randomUUID(),
                "Vinicius",
                new CPF("52998224725")
        );

        Cliente clienteDois = new Cliente(
                UUID.randomUUID(),
                "Peres",
                new CPF("11144477735")
        );

        when(clienteService.lista())
                .thenReturn(List.of(clienteUm, clienteDois));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Vinicius"))
                .andExpect(jsonPath("$[0].cpf").value("52998224725"))
                .andExpect(jsonPath("$[1].nome").value("Peres"))
                .andExpect(jsonPath("$[1].cpf").value("11144477735"));

        verify(clienteService).lista();
    }

    @Test
    void deveBuscarClientePorIdQuandoClienteExistir() throws Exception {
        UUID id = UUID.randomUUID();

        Cliente cliente = new Cliente(
                id,
                "Vinicius",
                new CPF(CPF_VALIDO)
        );

        when(clienteService.buscarPorId(id))
                .thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/clientes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.nome").value("Vinicius"))
                .andExpect(jsonPath("$.cpf").value(CPF_VALIDO));

        verify(clienteService).buscarPorId(id);
    }

    @Test
    void deveRetornar404QuandoClienteNaoExistir() throws Exception {
        UUID id = UUID.randomUUID();

        when(clienteService.buscarPorId(id))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/clientes/{id}", id))
                .andExpect(status().isNotFound());

        verify(clienteService).buscarPorId(id);
    }
}