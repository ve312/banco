package com.trinity.banco.infrastructure.repository;

import com.trinity.banco.infrastructure.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaClienteRepository extends JpaRepository<ClienteEntity, Long> {
    boolean existsByNumeroIdentificacion(String numeroIdentificacion);
}
