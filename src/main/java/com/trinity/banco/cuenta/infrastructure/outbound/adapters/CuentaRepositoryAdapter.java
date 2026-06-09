package com.trinity.banco.cuenta.infrastructure.outbound.adapters;

import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import com.trinity.banco.cuenta.infrastructure.outbound.persistence.mappers.CuentaMapper;
import com.trinity.banco.cuenta.infrastructure.outbound.persistence.jpaRepository.CuentaJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class CuentaRepositoryAdapter implements CuentaRepository {

    private final CuentaJpaRepository jpaRepository;

    public CuentaRepositoryAdapter(CuentaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }


    @Override
    public Cuenta guardar(Cuenta cuenta) {
        return CuentaMapper.toDomain(
                jpaRepository.save(CuentaMapper.toEntity(cuenta))
        );
    }

    @Override
    public Optional<Cuenta> buscarPorNumeroCuenta(String numeroCuenta) {
        return jpaRepository.findByNumeroCuenta(numeroCuenta)
                .map(CuentaMapper::toDomain);
    }

    @Override
    public boolean existePorNumeroCuenta(String numeroCuenta) {
        return jpaRepository.existsByNumeroCuenta(numeroCuenta);
    }

    @Override
    public List<Cuenta> listarPorClienteId(Long clienteId) {
        return jpaRepository.findByClienteId(clienteId)
                .stream()
                .map(CuentaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existePorClienteId(Long clienteId) {
        return jpaRepository.existsByClienteId(clienteId);
    }
}
