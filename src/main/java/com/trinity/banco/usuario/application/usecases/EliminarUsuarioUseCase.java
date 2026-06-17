package com.trinity.banco.usuario.application.usecases;

import com.trinity.banco.usuario.domain.ports.UsuarioRepository;

public class EliminarUsuarioUseCase {
    private final UsuarioRepository usuarioRepository;

    public EliminarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void ejecutar(Long id) {
        if (!usuarioRepository.existePorId(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.eliminar(id);
    }
}
