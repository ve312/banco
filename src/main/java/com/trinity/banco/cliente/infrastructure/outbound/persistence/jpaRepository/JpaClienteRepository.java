package com.trinity.banco.cliente.infrastructure.outbound.persistence.jpaRepository;

import com.trinity.banco.cliente.infrastructure.outbound.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaClienteRepository extends JpaRepository<ClienteEntity, Long> {
    boolean existsByNumeroIdentificacion(String numeroIdentificacion);
    boolean existsByEmail(String email);
}
