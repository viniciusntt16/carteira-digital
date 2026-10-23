package org.example.repositorios;

import org.example.entidades.Cliente;
import org.example.objetos.CPF;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {
    Optional<Cliente> findByCpf(CPF cpf);
}
