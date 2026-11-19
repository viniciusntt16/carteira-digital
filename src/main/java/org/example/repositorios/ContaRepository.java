package org.example.repositorios;

import org.example.entidades.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContaRepository extends JpaRepository<Conta, Integer> {
    @Query("""
        SELECT c
        FROM Conta c
        WHERE NOT EXISTS (
            SELECT t
            FROM Transacao t
            WHERE t.contaOrigem = c
               OR t.contaDestino = c
        )
        ORDER BY c.numero
        """)
    List<Conta> buscarContasSemMovimentacao();
}