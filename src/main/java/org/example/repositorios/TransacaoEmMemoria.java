package org.example.repositorios;

import org.example.entidades.Transacao;

import java.util.*;

public class TransacaoEmMemoria implements TransacaoRepository {
    private final Map<UUID, Transacao> transacaoMap = new LinkedHashMap<>();
    @Override
    public Transacao salvar(Transacao transacao) {
        Objects.requireNonNull(transacao, "A transação não pode ser null");
        transacaoMap.put(transacao.getId(), transacao);
        return transacao;
    }

    @Override
    public List<Transacao> lista() {
        return List.copyOf(transacaoMap.values());
    }
}
