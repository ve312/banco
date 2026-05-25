package com.trinity.banco.transaccion.infrastructure.outbound.adapters;

import com.trinity.banco.transaccion.domain.model.Transaccion;
import com.trinity.banco.transaccion.domain.ports.TransaccionRepository;
import com.trinity.banco.transaccion.infrastructure.outbound.persistence.mappers.TransaccionMapper;
import com.trinity.banco.transaccion.infrastructure.outbound.persistence.jpaRepository.TransaccionJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class TransaccionRepositoryAdapter implements TransaccionRepository {

    private final TransaccionJpaRepository jpaRepository;

    public TransaccionRepositoryAdapter(TransaccionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Transaccion guardar(Transaccion transaccion) {
        return TransaccionMapper.toDomain(
                jpaRepository.save(TransaccionMapper.toEntity(transaccion))
        );
    }

    @Override
    public List<Transaccion> listarPorNumeroCuenta(String numeroCuenta) {
        return jpaRepository.findByNumeroCuenta(numeroCuenta)
                .stream()
                .map(TransaccionMapper::toDomain)
                .collect(Collectors.toList());
    }
}
