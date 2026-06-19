package com.trinity.banco.unit.usuario;

import com.trinity.banco.usuario.application.usecases.ListarUsuariosUseCase;
import com.trinity.banco.usuario.domain.model.Usuario;
import com.trinity.banco.usuario.domain.model.enums.Rol;
import com.trinity.banco.usuario.domain.ports.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListarUsuariosServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ListarUsuariosUseCase listarUsuariosService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_listar_usuarios_exitosamente() {
        List<Usuario> usuarios = List.of(
                new Usuario(1L, "admin", "pass", "Admin", "User", true, Rol.ADMIN,
                        LocalDateTime.now(), LocalDateTime.now()),
                new Usuario(2L, "asesor", "pass", "Asesor", "User", true, Rol.ASESOR,
                        LocalDateTime.now(), LocalDateTime.now())
        );
        when(usuarioRepository.listar()).thenReturn(usuarios);

        List<Usuario> resultado = listarUsuariosService.ejecutar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(usuarioRepository, times(1)).listar();
    }

    @Test
    void deberia_retornar_lista_vacia_si_no_hay_usuarios() {
        when(usuarioRepository.listar()).thenReturn(List.of());

        List<Usuario> resultado = listarUsuariosService.ejecutar();

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
        verify(usuarioRepository, times(1)).listar();
    }
}
