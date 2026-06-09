package com.trinity.banco.cliente.infrastructure.outbound.adapters;

import com.trinity.banco.cliente.domain.model.Cliente;
import com.trinity.banco.cliente.domain.ports.ClienteRepository;
import com.trinity.banco.cliente.infrastructure.outbound.persistence.mappers.ClienteMapper;
import com.trinity.banco.cliente.infrastructure.outbound.persistence.entity.ClienteEntity;
import com.trinity.banco.cliente.infrastructure.outbound.persistence.jpaRepository.JpaClienteRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepositoryAdapter implements ClienteRepository {

    private final JpaClienteRepository jpaRepository;

    public ClienteRepositoryAdapter(JpaClienteRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Cliente guardar(Cliente cliente) {
        ClienteEntity entity = ClienteMapper.toEntity(cliente);
        ClienteEntity saved = jpaRepository.save(entity);
        return ClienteMapper.toDomain(saved);
    }

    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        return jpaRepository.findById(id)
                .map(ClienteMapper::toDomain);
    }

    @Override
    public List<Cliente> listar() {
        return jpaRepository.findAll()
                .stream()
                .map(ClienteMapper::toDomain)
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorNumeroIdentificacion(String numeroIdentificacion) {
        return jpaRepository.existsByNumeroIdentificacion(numeroIdentificacion);
    }

    @Override
    public boolean existePorId(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public boolean existePorEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

}
