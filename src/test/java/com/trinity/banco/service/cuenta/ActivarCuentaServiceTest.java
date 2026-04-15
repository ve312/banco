package com.trinity.banco.service.cuenta;

import com.trinity.banco.application.service.cuenta.ActivarCuentaService;
import com.trinity.banco.domain.model.Cuenta;
import com.trinity.banco.domain.ports.repository.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ActivarCuentaServiceTest {
    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private ActivarCuentaService activarCuentaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_activar_cuenta_exitosamente() {
        Cuenta cuentaMock = mock(Cuenta.class);
        when(cuentaRepository.buscarPorNumeroCuenta("5300000001")).thenReturn(Optional.of(cuentaMock));

        activarCuentaService.ejecutar("5300000001");

        verify(cuentaMock, times(1)).activar();
        verify(cuentaRepository, times(1)).guardar(cuentaMock);
    }

    @Test
    void deberia_lanzar_error_si_cuenta_no_existe() {
        when(cuentaRepository.buscarPorNumeroCuenta("5300000001")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                activarCuentaService.ejecutar("5300000001")
        );

        assertEquals("Cuenta no encontrada", ex.getMessage());
    }
}
