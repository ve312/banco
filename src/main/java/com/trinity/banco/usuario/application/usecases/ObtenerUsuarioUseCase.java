package com.trinity.banco.usuario.application.usecases;

import com.trinity.banco.shared.domain.errors.RecursoNoEncontradoException;
import com.trinity.banco.usuario.domain.model.Usuario;
import com.trinity.banco.usuario.domain.ports.UsuarioRepository;

public class ObtenerUsuarioUseCase {
    private final UsuarioRepository usuarioRepository;

    public ObtenerUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario ejecutar(Long id) {
        return usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));
    }
}
