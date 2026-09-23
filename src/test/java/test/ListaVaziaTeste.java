package test;

import org.example.Repositorios.ClienteEmMemoria;
import org.example.Repositorios.ClienteRepository;
import org.example.Repositorios.ContaEmMemoria;
import org.example.Repositorios.ContaRepository;
import org.example.entidades.Cliente;
import org.example.entidades.Conta;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ListaVaziaTeste {
    @Test
    void ListaVaziaCliente(){
        ClienteRepository clienteRepository = new ClienteEmMemoria();
        List<Cliente> resultado = clienteRepository.listaClientes();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void ListaVaziaConta(){
        ContaRepository contaRepository = new ContaEmMemoria();
        List<Conta> resultado = contaRepository.listaContas();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
