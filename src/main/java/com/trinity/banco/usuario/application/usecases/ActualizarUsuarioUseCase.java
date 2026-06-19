package com.trinity.banco.usuario.application.usecases;

import com.trinity.banco.shared.domain.errors.RecursoNoEncontradoException;
import com.trinity.banco.usuario.application.validators.UsuarioValidator;
import com.trinity.banco.usuario.domain.model.Usuario;
import com.trinity.banco.usuario.domain.model.enums.Rol;
import com.trinity.banco.usuario.domain.ports.UsuarioRepository;

public class ActualizarUsuarioUseCase {
    private final UsuarioRepository usuarioRepository;

    public ActualizarUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario ejecutar(Long id, String nombre, String apellido, Rol rol, boolean activo) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));

        UsuarioValidator.validarNombre(nombre, apellido);

        usuario.actualizarDatos(nombre, apellido, rol, activo);

        return usuarioRepository.guardar(usuario);
    }
}
