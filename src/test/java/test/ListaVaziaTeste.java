package test;

import org.example.repositorios.ClienteEmMemoria;
import org.example.repositorios.ClienteRepository;
import org.example.repositorios.ContaEmMemoria;
import org.example.repositorios.ContaRepository;
import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListaVaziaTeste {
    @Test
    void listaVaziaCliente(){
        ClienteRepository clienteRepository = new ClienteEmMemoria();
        List<Cliente> resultado = clienteRepository.lista();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void listaVaziaConta(){
        ContaRepository contaRepository = new ContaEmMemoria();
        List<Conta> resultado = contaRepository.lista();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
