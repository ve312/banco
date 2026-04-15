package com.trinity.banco.service.cliente;

import com.trinity.banco.application.service.cliente.ObtenerClienteService;
import com.trinity.banco.domain.model.Cliente;
import com.trinity.banco.domain.ports.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ObtenerClienteServiceTest {
    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ObtenerClienteService obtenerClienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_obtener_cliente_exitosamente() {
        Cliente clienteMock = mock(Cliente.class);
        when(clienteRepository.buscarPorId(1L)).thenReturn(Optional.of(clienteMock));

        Cliente resultado = obtenerClienteService.ejecutar(1L);

        assertNotNull(resultado);
        verify(clienteRepository, times(1)).buscarPorId(1L);
    }

    @Test
    void deberia_lanzar_error_si_cliente_no_existe() {
        when(clienteRepository.buscarPorId(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                obtenerClienteService.ejecutar(1L)
        );

        assertEquals("Cliente no encontrado", ex.getMessage());
        verify(clienteRepository, times(1)).buscarPorId(1L);
    }
}
