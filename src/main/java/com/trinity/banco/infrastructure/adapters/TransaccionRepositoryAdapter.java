package com.trinity.banco.infrastructure.adapters;

import com.trinity.banco.domain.model.Transaccion;
import com.trinity.banco.domain.ports.repository.TransaccionRepository;
import com.trinity.banco.infrastructure.adapters.mapper.TransaccionMapper;
import com.trinity.banco.infrastructure.repository.TransaccionJpaRepository;
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
