package com.trinity.banco.shared.infrastructure.security;

import com.trinity.banco.usuario.domain.model.Usuario;
import com.trinity.banco.usuario.domain.model.enums.Rol;
import com.trinity.banco.usuario.domain.ports.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!usuarioRepository.existePorUsername("admin")) {
            String passwordEncriptada = passwordEncoder.encode("admin123");

            Usuario admin = new Usuario(
                    null,
                    "admin",
                    passwordEncriptada,
                    "Administrador",
                    "del Sistema",
                    true,
                    Rol.ADMIN,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            usuarioRepository.guardar(admin);
            log.info("Usuario administrador creado: admin");
        } else {
            log.info("El usuario administrador ya existe, se omite la creación");
        }
    }
}
