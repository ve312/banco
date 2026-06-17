package com.trinity.banco.usuario.infrastructure.outbound.adapters;

import com.trinity.banco.usuario.domain.model.Usuario;
import com.trinity.banco.usuario.domain.ports.UsuarioRepository;
import com.trinity.banco.usuario.infrastructure.outbound.persistence.mappers.UsuarioMapper;
import com.trinity.banco.usuario.infrastructure.outbound.persistence.entity.UsuarioEntity;
import com.trinity.banco.usuario.infrastructure.outbound.persistence.jpaRepository.JpaUsuarioRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioRepositoryAdapter implements UsuarioRepository {

    private final JpaUsuarioRepository jpaRepository;

    public UsuarioRepositoryAdapter(JpaUsuarioRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        UsuarioEntity entity = UsuarioMapper.toEntity(usuario);
        UsuarioEntity saved = jpaRepository.save(entity);
        return UsuarioMapper.toDomain(saved);
    }

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        return jpaRepository.findById(id)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public Optional<Usuario> buscarPorUsername(String username) {
        return jpaRepository.findByUsername(username)
                .map(UsuarioMapper::toDomain);
    }

    @Override
    public List<Usuario> listar() {
        return jpaRepository.findAll()
                .stream()
                .map(UsuarioMapper::toDomain)
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existePorUsername(String username) {
        return jpaRepository.existsByUsername(username);
    }

    @Override
    public boolean existePorId(Long id) {
        return jpaRepository.existsById(id);
    }
}
