package com.trinity.banco.infrastructure.repository;

import com.trinity.banco.infrastructure.entity.TransaccionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransaccionJpaRepository extends JpaRepository<TransaccionEntity, Long> {

    List<TransaccionEntity> findByNumeroCuenta(String numeroCuenta);
}
