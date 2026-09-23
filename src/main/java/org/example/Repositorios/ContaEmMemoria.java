package org.example.Repositorios;

import org.example.entidades.Conta;

import java.util.*;

public class ContaEmMemoria implements ContaRepository{
    private final Map<Integer, Conta> contaMap = new HashMap<>();
    @Override
    public Conta salvar(Conta conta) {
        Objects.requireNonNull(conta, "A conta não pode ser null");
        contaMap.put(conta.getNumero(), conta);
        return conta;
    }

    @Override
    public Optional<Conta> buscarPorId(int numero) {
        return Optional.ofNullable(contaMap.get(numero));
    }

    @Override
    public List<Conta> listaContas() {
        return List.copyOf(contaMap.values());
    }
}
