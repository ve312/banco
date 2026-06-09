package com.trinity.banco.cuenta.infrastructure.outbound.persistence.jpaRepository;

import com.trinity.banco.cuenta.infrastructure.outbound.persistence.entity.CuentaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CuentaJpaRepository extends JpaRepository<CuentaEntity,Long> {
    Optional<CuentaEntity> findByNumeroCuenta(String numeroCuenta);

    boolean existsByNumeroCuenta(String numeroCuenta);

    List<CuentaEntity> findByClienteId(Long clienteId);

    boolean existsByClienteId(Long clienteId);
}
