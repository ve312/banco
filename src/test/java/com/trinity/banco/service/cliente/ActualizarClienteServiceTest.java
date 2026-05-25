package com.trinity.banco.service.cliente;

import com.trinity.banco.cliente.application.usecases.ActualizarClienteService;
import com.trinity.banco.cliente.domain.model.Cliente;
import com.trinity.banco.cliente.domain.ports.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ActualizarClienteServiceTest {
    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ActualizarClienteService actualizarClienteService;

    private Cliente clienteMock;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clienteMock = mock(Cliente.class);
    }

    @Test
    void deberia_actualizar_cliente_exitosamente() {
        when(clienteRepository.buscarPorId(1L)).thenReturn(Optional.of(clienteMock));
        when(clienteRepository.guardar(any(Cliente.class))).thenReturn(clienteMock);


        Cliente resultado = actualizarClienteService.ejecutar(1L, "Juan", "Perez", "juan.perez@gmail.com");

        assertNotNull(resultado);
        verify(clienteMock, times(1)).actualizarDatos("Juan", "Perez", "juan.perez@gmail.com");
        verify(clienteRepository, times(1)).guardar(clienteMock);
    }

    @Test
    void deberia_lanzar_error_si_cliente_no_existe() {
        when(clienteRepository.buscarPorId(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                actualizarClienteService.ejecutar(1L, "Juan", "Perez", "juan.perez@gmail.com")
        );

        assertEquals("Cliente no encontrado", ex.getMessage());
        verify(clienteRepository, never()).guardar(any());
    }
}
