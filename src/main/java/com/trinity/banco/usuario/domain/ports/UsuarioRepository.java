package com.trinity.banco.usuario.domain.ports;

import com.trinity.banco.usuario.domain.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository {
    Usuario guardar(Usuario usuario);

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorUsername(String username);

    List<Usuario> listar();

    void eliminar(Long id);

    boolean existePorUsername(String username);

    boolean existePorId(Long id);
}
