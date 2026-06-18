package com.trinity.banco.unit.usuario;

import com.trinity.banco.usuario.application.usecases.CambiarPasswordUseCase;
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

class CambiarPasswordServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CambiarPasswordUseCase cambiarPasswordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_cambiar_password_exitosamente() {
        Usuario usuario = new Usuario(1L, "jperez", "oldpass", "Juan", "Perez",
                true, Rol.ASESOR, LocalDateTime.now(), LocalDateTime.now());
        when(usuarioRepository.buscarPorId(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.guardar(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = cambiarPasswordService.ejecutar(1L, "newpassword123");

        assertNotNull(resultado);
        assertEquals("newpassword123", resultado.getPassword());
        verify(usuarioRepository, times(1)).guardar(usuario);
    }

    @Test
    void deberia_lanzar_error_si_usuario_no_existe() {
        when(usuarioRepository.buscarPorId(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                cambiarPasswordService.ejecutar(99L, "newpassword123")
        );

        assertEquals("Usuario no encontrado con id: 99", ex.getMessage());
        verify(usuarioRepository, never()).guardar(any());
    }

    @Test
    void deberia_lanzar_error_si_password_corta() {
        Usuario usuario = new Usuario(1L, "jperez", "oldpass", "Juan", "Perez",
                true, Rol.ASESOR, LocalDateTime.now(), LocalDateTime.now());
        when(usuarioRepository.buscarPorId(1L)).thenReturn(Optional.of(usuario));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                cambiarPasswordService.ejecutar(1L, "12345")
        );

        assertEquals("La contraseña debe tener al menos 6 caracteres", ex.getMessage());
        verify(usuarioRepository, never()).guardar(any());
    }

    @Test
    void deberia_lanzar_error_si_password_nula() {
        Usuario usuario = new Usuario(1L, "jperez", "oldpass", "Juan", "Perez",
                true, Rol.ASESOR, LocalDateTime.now(), LocalDateTime.now());
        when(usuarioRepository.buscarPorId(1L)).thenReturn(Optional.of(usuario));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                cambiarPasswordService.ejecutar(1L, null)
        );

        assertEquals("La contraseña debe tener al menos 6 caracteres", ex.getMessage());
        verify(usuarioRepository, never()).guardar(any());
    }
}
