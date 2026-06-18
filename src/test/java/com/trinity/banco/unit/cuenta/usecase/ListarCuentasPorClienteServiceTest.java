package com.trinity.banco.unit.cuenta.usecase;

import com.trinity.banco.cuenta.application.usecases.ListarCuentasPorClienteUseCase;
import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cliente.domain.ports.ClienteRepository;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ListarCuentasPorClienteServiceTest {
    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ListarCuentasPorClienteUseCase listarCuentasPorClienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_listar_cuentas_del_cliente_exitosamente() {
        when(clienteRepository.existePorId(1L)).thenReturn(true);
        List<Cuenta> cuentas = Arrays.asList(mock(Cuenta.class), mock(Cuenta.class));
        when(cuentaRepository.listarPorClienteId(1L)).thenReturn(cuentas);

        List<Cuenta> resultado = listarCuentasPorClienteService.ejecutar(1L);

        assertEquals(2, resultado.size());
        verify(cuentaRepository, times(1)).listarPorClienteId(1L);
    }

    @Test
    void deberia_lanzar_error_si_cliente_no_existe_al_listar() {
        when(clienteRepository.existePorId(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                listarCuentasPorClienteService.ejecutar(1L)
        );

        assertEquals("Cliente no encontrado", ex.getMessage());
        verify(cuentaRepository, never()).listarPorClienteId(anyLong());
    }
}
