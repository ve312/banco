package com.trinity.banco.usuario.infrastructure.outbound.persistence.jpaRepository;

import com.trinity.banco.usuario.infrastructure.outbound.persistence.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByUsername(String username);

    boolean existsByUsername(String username);
}
