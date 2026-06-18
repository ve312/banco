package com.trinity.banco.unit.usuario;

import com.trinity.banco.usuario.application.usecases.ObtenerUsuarioUseCase;
import com.trinity.banco.usuario.domain.model.Usuario;
import com.trinity.banco.usuario.domain.model.enums.Rol;
import com.trinity.banco.usuario.domain.ports.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ObtenerUsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ObtenerUsuarioUseCase obtenerUsuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_obtener_usuario_exitosamente() {
        Usuario usuario = new Usuario(1L, "jperez", "123456", "Juan", "Perez", true, Rol.ASESOR, LocalDateTime.now(), LocalDateTime.now());
        when(usuarioRepository.buscarPorId(1L)).thenReturn(Optional.of(usuario));

        Usuario resultado = obtenerUsuarioService.ejecutar(1L);

        assertNotNull(resultado);
        assertEquals("jperez", resultado.getUsername());
        assertEquals(Rol.ASESOR, resultado.getRol());
    }

    @Test
    void deberia_lanzar_error_si_usuario_no_existe() {
        when(usuarioRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                obtenerUsuarioService.ejecutar(99L)
        );

        assertEquals("Usuario no encontrado con id: 99", ex.getMessage());
    }
}
