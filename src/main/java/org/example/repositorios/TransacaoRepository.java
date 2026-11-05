package org.example.repositorios;

import org.example.entidades.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {

    @Query("""
        SELECT t
        FROM Transacao t
        LEFT JOIN t.contaDestino destino
        WHERE t.contaOrigem.numero = :numeroConta
           OR destino.numero = :numeroConta
        ORDER BY t.dataHora DESC
        """)
    List<Transacao> buscarPorContaOrdenadoPorData(
            @Param("numeroConta") int numeroConta
    );
}
