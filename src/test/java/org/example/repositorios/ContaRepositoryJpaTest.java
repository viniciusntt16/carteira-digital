package org.example.repositorios;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.objetos.CPF;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(showSql = false)
class ContaRepositoryJpaTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContaRepository contaRepository;

    @Test
    void deveSalvarEBuscarConta() {
        Cliente cliente = new Cliente(
                UUID.randomUUID(),
                "Vinicius",
                new CPF("12345678910")
        );

        clienteRepository.saveAndFlush(cliente);

        Conta conta = new Conta(
                1,
                cliente,
                BigDecimal.valueOf(2000)
        );

        contaRepository.saveAndFlush(conta);

        Conta contaEncontrada = contaRepository.findById(1)
                .orElseThrow();

        assertNotNull(contaEncontrada);
        assertEquals(1, contaEncontrada.getNumero());

        assertEquals(
                BigDecimal.valueOf(2000)
                        .setScale(2, RoundingMode.HALF_EVEN),
                contaEncontrada.getSaldo()
        );

        assertEquals(cliente.getId(), contaEncontrada.getCliente().getId());
    }

    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremContas() {
        assertTrue(contaRepository.findAll().isEmpty());
    }

}
