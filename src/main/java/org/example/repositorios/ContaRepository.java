package org.example.repositorios;

import org.example.entidades.Conta;

import java.util.List;
import java.util.Optional;

public interface ContaRepository {
    Conta salvar(Conta conta);
    Optional<Conta> buscarPorId(int numero);
    List<Conta> lista();

}
