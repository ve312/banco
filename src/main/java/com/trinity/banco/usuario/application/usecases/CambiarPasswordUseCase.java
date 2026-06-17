package com.trinity.banco.usuario.application.usecases;

import com.trinity.banco.shared.domain.errors.RecursoNoEncontradoException;
import com.trinity.banco.usuario.application.validators.UsuarioValidator;
import com.trinity.banco.usuario.domain.model.Usuario;
import com.trinity.banco.usuario.domain.ports.UsuarioRepository;

public class CambiarPasswordUseCase {
    private final UsuarioRepository usuarioRepository;

    public CambiarPasswordUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario ejecutar(Long id, String nuevaPassword) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));

        UsuarioValidator.validarPassword(nuevaPassword);

        usuario.actualizarPassword(nuevaPassword);

        return usuarioRepository.guardar(usuario);
    }
}
