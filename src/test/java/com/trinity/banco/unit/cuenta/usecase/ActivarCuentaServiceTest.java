package com.trinity.banco.unit.cuenta.usecase;

import com.trinity.banco.cuenta.application.usecases.ActivarCuentaUseCase;
import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cuenta.domain.model.enums.EstadoCuenta;
import com.trinity.banco.cuenta.domain.model.enums.TipoCuenta;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class ActivarCuentaServiceTest {
    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private ActivarCuentaUseCase activarCuentaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_activar_cuenta_exitosamente() {
        Cuenta cuenta = new Cuenta(1L, TipoCuenta.AHORROS, "5300000001", EstadoCuenta.INACTIVA, BigDecimal.ZERO, false, LocalDateTime.now(), LocalDateTime.now(), 1L);
        when(cuentaRepository.buscarPorNumeroCuenta("5300000001")).thenReturn(Optional.of(cuenta));

        activarCuentaService.ejecutar("5300000001");

        assertEquals(EstadoCuenta.ACTIVA, cuenta.getEstado());
        verify(cuentaRepository, times(1)).guardar(cuenta);
    }

    @Test
    void deberia_lanzar_error_si_cuenta_no_existe() {
        when(cuentaRepository.buscarPorNumeroCuenta("5300000001")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                activarCuentaService.ejecutar("5300000001")
        );

        assertEquals("Cuenta no encontrada", ex.getMessage());
    }

    @Test
    void deberia_lanzar_error_si_cuenta_ya_esta_activa() {
        Cuenta cuenta = new Cuenta(1L, TipoCuenta.AHORROS, "5300000001", EstadoCuenta.ACTIVA, BigDecimal.ZERO, false, LocalDateTime.now(), LocalDateTime.now(), 1L);
        when(cuentaRepository.buscarPorNumeroCuenta("5300000001")).thenReturn(Optional.of(cuenta));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                activarCuentaService.ejecutar("5300000001")
        );

        assertEquals("La cuenta ya está activa", ex.getMessage());
    }
}
