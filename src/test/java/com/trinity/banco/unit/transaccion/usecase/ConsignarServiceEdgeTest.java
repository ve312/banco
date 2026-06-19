package com.trinity.banco.unit.transaccion.usecase;

import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cuenta.domain.model.enums.EstadoCuenta;
import com.trinity.banco.cuenta.domain.model.enums.TipoCuenta;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import com.trinity.banco.transaccion.application.usecases.ConsignarUseCase;
import com.trinity.banco.transaccion.domain.model.Transaccion;
import com.trinity.banco.transaccion.domain.ports.TransaccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConsignarServiceEdgeTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private TransaccionRepository transaccionRepository;

    @InjectMocks
    private ConsignarUseCase consignarService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_lanzar_error_si_cuenta_esta_inactiva() {
        Cuenta cuentaInactiva = new Cuenta(1L, TipoCuenta.AHORROS, "5300000001",
                EstadoCuenta.INACTIVA, new BigDecimal("1000"), false,
                LocalDateTime.now(), LocalDateTime.now(), 1L);
        when(cuentaRepository.buscarPorNumeroCuenta("5300000001")).thenReturn(Optional.of(cuentaInactiva));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                consignarService.ejecutar("5300000001", new BigDecimal("500"))
        );

        assertEquals("La cuenta no está activa", ex.getMessage());
        verify(cuentaRepository, never()).guardar(any());
    }

    @Test
    void deberia_lanzar_error_si_cuenta_esta_cancelada() {
        Cuenta cuentaCancelada = new Cuenta(1L, TipoCuenta.AHORROS, "5300000001",
                EstadoCuenta.CANCELADA, new BigDecimal("1000"), false,
                LocalDateTime.now(), LocalDateTime.now(), 1L);
        when(cuentaRepository.buscarPorNumeroCuenta("5300000001")).thenReturn(Optional.of(cuentaCancelada));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                consignarService.ejecutar("5300000001", new BigDecimal("500"))
        );

        assertEquals("La cuenta no está activa", ex.getMessage());
    }

    @Test
    void deberia_lanzar_error_si_cuenta_no_existe_consignar() {
        when(cuentaRepository.buscarPorNumeroCuenta("999")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                consignarService.ejecutar("999", new BigDecimal("500"))
        );

        assertEquals("Cuenta no encontrada", ex.getMessage());
    }
}
