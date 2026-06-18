package com.trinity.banco.unit.cliente.usecase;

import com.trinity.banco.cliente.application.usecases.EliminarClienteUseCase;
import com.trinity.banco.cliente.domain.ports.ClienteRepository;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class EliminarClienteServiceTest {
    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private EliminarClienteUseCase eliminarClienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_eliminar_cliente_exitosamente() {
        when(clienteRepository.existePorId(1L)).thenReturn(true);
        when(cuentaRepository.existePorClienteId(1L)).thenReturn(false);

        eliminarClienteService.ejecutar(1L);

        verify(clienteRepository, times(1)).eliminar(1L);
    }

    @Test
    void deberia_lanzar_error_si_cliente_no_existe() {
        when(clienteRepository.existePorId(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                eliminarClienteService.ejecutar(1L)
        );

        assertEquals("Cliente no encontrado", ex.getMessage());
        verify(cuentaRepository, never()).existePorClienteId(anyLong());
        verify(clienteRepository, never()).eliminar(anyLong());
    }

    @Test
    void deberia_lanzar_error_si_cliente_tiene_cuentas_asociadas() {
        when(clienteRepository.existePorId(1L)).thenReturn(true);
        when(cuentaRepository.existePorClienteId(1L)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                eliminarClienteService.ejecutar(1L)
        );

        assertEquals("No se puede eliminar el cliente porque tiene cuentas asociadas", ex.getMessage());
        verify(clienteRepository, never()).eliminar(anyLong());
    }
}
