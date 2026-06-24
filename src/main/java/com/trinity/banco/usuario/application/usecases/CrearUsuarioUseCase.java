package com.trinity.banco.usuario.application.usecases;

import com.trinity.banco.usuario.application.validators.UsuarioValidator;
import com.trinity.banco.usuario.domain.model.Usuario;
import com.trinity.banco.usuario.domain.model.enums.Rol;
import com.trinity.banco.usuario.domain.ports.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

public class CrearUsuarioUseCase {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public CrearUsuarioUseCase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario ejecutar(String username, String password, String nombre, String apellido, Rol rol) {
        if (usuarioRepository.existePorUsername(username)) {
            throw new RuntimeException("El username '" + username + "' ya está en uso");
        }

        UsuarioValidator.validarUsername(username);
        UsuarioValidator.validarPassword(password);
        UsuarioValidator.validarNombre(nombre, apellido);

        String passwordEncriptada = passwordEncoder.encode(password);

        Usuario usuario = new Usuario(
                null,
                username,
                passwordEncriptada,
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
