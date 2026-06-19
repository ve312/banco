package com.trinity.banco.unit.usuario;

import com.trinity.banco.usuario.application.usecases.CrearUsuarioUseCase;
import com.trinity.banco.usuario.domain.model.Usuario;
import com.trinity.banco.usuario.domain.model.enums.Rol;
import com.trinity.banco.usuario.domain.ports.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CrearUsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private CrearUsuarioUseCase crearUsuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_crear_usuario_exitosamente() {
        when(usuarioRepository.existePorUsername("jperez")).thenReturn(false);
        when(usuarioRepository.guardar(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = crearUsuarioService.ejecutar(
                "jperez",
                "123456",
                "Juan",
                "Perez",
                Rol.ASESOR
        );

        assertNotNull(resultado);
        assertEquals("jperez", resultado.getUsername());
        assertEquals("123456", resultado.getPassword());
        assertEquals(Rol.ASESOR, resultado.getRol());
        assertTrue(resultado.isActivo());
        verify(usuarioRepository, times(1)).guardar(any(Usuario.class));
    }

    @Test
    void deberia_lanzar_error_si_username_ya_existe() {
        when(usuarioRepository.existePorUsername("jperez")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                crearUsuarioService.ejecutar("jperez", "123456", "Juan", "Perez", Rol.ASESOR)
        );

        assertEquals("El username 'jperez' ya está en uso", ex.getMessage());
        verify(usuarioRepository, never()).guardar(any());
    }

    @Test
    void deberia_lanzar_error_si_username_muy_corto() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                crearUsuarioService.ejecutar("jp", "123456", "Juan", "Perez", Rol.ASESOR)
        );

        assertEquals("El username debe tener al menos 3 caracteres", ex.getMessage());
        verify(usuarioRepository, never()).guardar(any());
    }

    @Test
    void deberia_lanzar_error_si_password_muy_corta() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                crearUsuarioService.ejecutar("jperez", "123", "Juan", "Perez", Rol.ASESOR)
        );

        assertEquals("La contraseña debe tener al menos 6 caracteres", ex.getMessage());
        verify(usuarioRepository, never()).guardar(any());
    }

    @Test
    void deberia_lanzar_error_si_nombre_vacio() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                crearUsuarioService.ejecutar("jperez", "123456", "", "Perez", Rol.ASESOR)
        );

        assertEquals("El nombre es obligatorio", ex.getMessage());
        verify(usuarioRepository, never()).guardar(any());
    }

    @Test
    void deberia_lanzar_error_si_apellido_vacio() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                crearUsuarioService.ejecutar("jperez", "123456", "Juan", "", Rol.ASESOR)
        );

        assertEquals("El apellido es obligatorio", ex.getMessage());
        verify(usuarioRepository, never()).guardar(any());
    }
}
