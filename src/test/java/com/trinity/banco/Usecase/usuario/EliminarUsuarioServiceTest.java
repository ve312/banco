package com.trinity.banco.Usecase.usuario;

import com.trinity.banco.usuario.application.usecases.EliminarUsuarioUseCase;
import com.trinity.banco.usuario.domain.ports.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EliminarUsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private EliminarUsuarioUseCase eliminarUsuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_eliminar_usuario_exitosamente() {
        when(usuarioRepository.existePorId(1L)).thenReturn(true);

        eliminarUsuarioService.ejecutar(1L);

        verify(usuarioRepository, times(1)).eliminar(1L);
    }

    @Test
    void deberia_lanzar_error_si_usuario_no_existe() {
        when(usuarioRepository.existePorId(99L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                eliminarUsuarioService.ejecutar(99L)
        );

        assertEquals("Usuario no encontrado con id: 99", ex.getMessage());
        verify(usuarioRepository, never()).eliminar(any());
    }
}
