package com.trinity.banco.unit.usuario;

import com.trinity.banco.usuario.application.usecases.ActualizarUsuarioUseCase;
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

public class ActualizarUsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ActualizarUsuarioUseCase actualizarUsuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_actualizar_usuario_exitosamente() {
        Usuario usuario = new Usuario(1L, "jperez", "123456", "Juan", "Perez", true, Rol.ASESOR, LocalDateTime.now(), LocalDateTime.now());
        when(usuarioRepository.buscarPorId(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.guardar(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Usuario resultado = actualizarUsuarioService.ejecutar(1L, "Juan Carlos", "Perez Lopez", Rol.ADMIN, true);

        assertNotNull(resultado);
        assertEquals("Juan Carlos", resultado.getNombre());
        assertEquals("Perez Lopez", resultado.getApellido());
        assertEquals(Rol.ADMIN, resultado.getRol());
        assertTrue(resultado.isActivo());
        verify(usuarioRepository, times(1)).guardar(any(Usuario.class));
    }
}
