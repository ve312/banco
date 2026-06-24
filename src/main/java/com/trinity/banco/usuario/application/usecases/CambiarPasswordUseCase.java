package com.trinity.banco.usuario.application.usecases;

import com.trinity.banco.shared.domain.errors.RecursoNoEncontradoException;
import com.trinity.banco.usuario.application.validators.UsuarioValidator;
import com.trinity.banco.usuario.domain.model.Usuario;
import com.trinity.banco.usuario.domain.ports.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CambiarPasswordUseCase {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public CambiarPasswordUseCase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario ejecutar(Long id, String nuevaPassword) {
        Usuario usuario = usuarioRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado con id: " + id));

        UsuarioValidator.validarPassword(nuevaPassword);

        String passwordEncriptada = passwordEncoder.encode(nuevaPassword);
        usuario.actualizarPassword(passwordEncriptada);

        return usuarioRepository.guardar(usuario);
    }
}
