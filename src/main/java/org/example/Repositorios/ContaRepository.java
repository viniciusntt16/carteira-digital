package org.example.Repositorios;

import org.example.entidades.Conta;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContaRepository {
    Conta salvar(Conta conta);
    Optional<Conta> buscarPorId(int numero);
    List<Conta> listaContas();

}
