package org.example.repositorios;

import org.example.entidades.Transacao;

import java.util.List;

public interface TransacaoRepository {
    Transacao salvar(Transacao transacao);
    List<Transacao> lista();
    }
