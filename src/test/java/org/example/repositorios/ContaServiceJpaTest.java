package org.example.repositorios;

import org.example.entidades.Transacao;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.objetos.CPF;
import org.example.servicos.ContaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@DataJpaTest(showSql = false)
@Import(ContaService.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class ContaServiceJpaTest {
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private  ContaRepository contaRepository;
    @Autowired
    private ContaService contaService;
    @MockitoBean
    private TransacaoRepository transacaoRepository;

    @Test
    void Saldo(){
        UUID clienteId = UUID.randomUUID();
        int numeroConta = 1001;

        Cliente cliente = new Cliente(
                clienteId,
                "Vinicius",
                new CPF("12345678910")
        );
        try {
            clienteRepository.saveAndFlush(cliente);
            Conta conta = new Conta(numeroConta, cliente, BigDecimal.valueOf(1000));
            contaRepository.saveAndFlush(conta);
            contaService.depositar(numeroConta, BigDecimal.valueOf(500));
            Conta conta1 = contaRepository.findById(numeroConta).orElseThrow();
            assertEquals(0, new BigDecimal("1500")
                    .compareTo(conta1.getSaldo()));
            verify(transacaoRepository).salvar(any(Transacao.class));
        }finally {
            contaRepository.deleteById(numeroConta);
            clienteRepository.deleteById(clienteId);
        }
    }
}
