package test;

import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.example.entidades.Transacao;
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

public class TransferenciaTeste {
    @Test
    void transferencia(){
        ContaRepository contaRepository = new ContaEmMemoria();
        TransacaoRepository transacaoRepository = new TransacaoEmMemoria();
        ContaService contaService = new ContaService(contaRepository, transacaoRepository);
        Cliente c1 = new Cliente(UUID.randomUUID(), "Vinicius",
                new CPF("12345678910"));
        Cliente c2 = new Cliente(UUID.randomUUID(), "Peres",
                new CPF("12345678911"));
        Conta conta = new Conta(1, c1, 1000);
        Conta conta2 = new Conta(2, c2, 2000);
        contaRepository.salvar(conta);
        contaRepository.salvar(conta2);
        contaService.transferir(2, 1, 1500);
        assertEquals(2500, conta.getSaldo());
        assertEquals(500, conta2.getSaldo());

        List<Transacao> transacaos = transacaoRepository.lista();
        assertEquals(1, transacaos.size());
        assertEquals(TIPO.TRANSFERENCIA, transacaos.get(0).getTipo());

    }
}
