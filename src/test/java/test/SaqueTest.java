package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.entidades.Transacao;
import org.example.exceptions.SaldoInsuficienteException;
import org.example.objetos.CPF;
import org.example.objetos.TIPO;
import org.example.repositorios.ContaEmMemoria;
import org.example.repositorios.ContaRepository;
import org.example.repositorios.TransacaoEmMemoria;
import org.example.repositorios.TransacaoRepository;
import org.example.servicos.ContaService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SaqueTest {

    @Test
    void saqueTeste(){
        ContaRepository contaRepository = new ContaEmMemoria();
        TransacaoRepository transacaoRepository = new TransacaoEmMemoria();
        ContaService contaService = new ContaService(contaRepository, transacaoRepository);
        Cliente cliente = new Cliente(UUID.randomUUID(), "Vinicius",
                new CPF("12345678910"));
        Conta conta = new Conta(1, cliente, 1000);
        contaRepository.salvar(conta);
        contaService.sacar(1,500);
        assertEquals(500, conta.getSaldo());

        List<Transacao> transacaos = transacaoRepository.lista();
        assertEquals(1, transacaos.size());
        assertEquals(TIPO.SAQUE, transacaos.get(0).getTipo());
    }

    @Test
    void saqueValorIncorreto(){
        ContaRepository contaRepository = new ContaEmMemoria();
        TransacaoRepository transacaoRepository = new TransacaoEmMemoria();
        ContaService contaService = new ContaService(contaRepository, transacaoRepository);
        Cliente cliente = new Cliente(UUID.randomUUID(), "Vinicius",
                new CPF("12345678910"));
        Conta conta = new Conta(1, cliente, 1000);
        contaRepository.salvar(conta);
        contaService.depositar(1, 500);

        assertThrows(IllegalArgumentException.class,
                ()->contaService.sacar(1, 0));
        assertThrows(IllegalArgumentException.class,
                ()->contaService.sacar(1, -1));
    }

    @Test
    void saqueNaoRegistraErro(){
        ContaRepository contaRepository = new ContaEmMemoria();
        TransacaoRepository transacaoRepository = new TransacaoEmMemoria();
        ContaService contaService = new ContaService(contaRepository, transacaoRepository);
        Cliente cliente = new Cliente(UUID.randomUUID(), "Vinicius",
                new CPF("12345678910"));
        Conta conta = new Conta(1, cliente, 1000);
        contaRepository.salvar(conta);

        assertThrows(SaldoInsuficienteException.class,
                ()->contaService.sacar(1,1500));
        assertEquals(0, transacaoRepository.lista().size());
    }
}
