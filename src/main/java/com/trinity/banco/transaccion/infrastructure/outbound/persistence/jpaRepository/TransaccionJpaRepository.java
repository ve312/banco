package com.trinity.banco.transaccion.infrastructure.outbound.persistence.jpaRepository;

import com.trinity.banco.transaccion.infrastructure.outbound.persistence.entity.TransaccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransaccionJpaRepository extends JpaRepository<TransaccionEntity, Long> {

    List<TransaccionEntity> findByNumeroCuenta(String numeroCuenta);
}
