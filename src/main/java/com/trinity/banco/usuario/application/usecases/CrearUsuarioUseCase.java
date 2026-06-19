package com.trinity.banco.usuario.application.usecases;

import com.trinity.banco.usuario.application.validators.UsuarioValidator;
import com.trinity.banco.usuario.domain.model.Usuario;
import com.trinity.banco.usuario.domain.model.enums.Rol;
import com.trinity.banco.usuario.domain.ports.UsuarioRepository;

import java.time.LocalDateTime;

public class CrearUsuarioUseCase {
    private final UsuarioRepository usuarioRepository;

    public CrearUsuarioUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Usuario ejecutar(String username, String password, String nombre, String apellido, Rol rol) {
        if (usuarioRepository.existePorUsername(username)) {
            throw new RuntimeException("El username '" + username + "' ya está en uso");
        }

        UsuarioValidator.validarUsername(username);
        UsuarioValidator.validarPassword(password);
        UsuarioValidator.validarNombre(nombre, apellido);

        Usuario usuario = new Usuario(
                null,
                username,
                password,
                nombre,
                apellido,
                true,
                rol,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        return usuarioRepository.guardar(usuario);
    }
}
