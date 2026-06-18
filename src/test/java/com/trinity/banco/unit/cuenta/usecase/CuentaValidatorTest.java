package com.trinity.banco.unit.cuenta.usecase;

import com.trinity.banco.cuenta.application.validators.CuentaValidator;
import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cuenta.domain.model.enums.EstadoCuenta;
import com.trinity.banco.cuenta.domain.model.enums.TipoCuenta;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CuentaValidatorTest {

    @Test
    void deberia_aceptar_tipo_cuenta_valido() {
        assertDoesNotThrow(() -> CuentaValidator.validarTipoCuenta(TipoCuenta.AHORROS));
    }

    @Test
    void deberia_rechazar_tipo_cuenta_nulo() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> CuentaValidator.validarTipoCuenta(null));
        assertEquals("El tipo de cuenta es obligatorio", ex.getMessage());
    }

    @Test
    void deberia_aceptar_saldo_inicial_valido_ahorros() {
        assertDoesNotThrow(() -> CuentaValidator.validarSaldoInicial(TipoCuenta.AHORROS, new BigDecimal("1000")));
    }

    @Test
    void deberia_rechazar_saldo_inicial_nulo() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> CuentaValidator.validarSaldoInicial(TipoCuenta.AHORROS, null));
        assertEquals("El saldo inicial es obligatorio", ex.getMessage());
    }

    @Test
    void deberia_rechazar_saldo_inicial_negativo_ahorros() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                CuentaValidator.validarSaldoInicial(TipoCuenta.AHORROS, new BigDecimal("-100"))
        );
        assertEquals("La cuenta de ahorros no puede iniciar con saldo negativo", ex.getMessage());
    }

    @Test
    void deberia_aceptar_saldo_cero_en_corriente() {
        assertDoesNotThrow(() -> CuentaValidator.validarSaldoInicial(TipoCuenta.CORRIENTE, BigDecimal.ZERO));
    }

    @Test
    void deberia_aceptar_monto_valido() {
        assertDoesNotThrow(() -> CuentaValidator.validarMonto(new BigDecimal("100")));
    }

    @Test
    void deberia_rechazar_monto_nulo() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> CuentaValidator.validarMonto(null));
        assertEquals("El monto debe ser mayor a cero", ex.getMessage());
    }

    @Test
    void deberia_rechazar_monto_cero() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> CuentaValidator.validarMonto(BigDecimal.ZERO));
        assertEquals("El monto debe ser mayor a cero", ex.getMessage());
    }

    @Test
    void deberia_rechazar_monto_negativo() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> CuentaValidator.validarMonto(new BigDecimal("-50")));
        assertEquals("El monto debe ser mayor a cero", ex.getMessage());
    }

    @Test
    void deberia_aceptar_cuenta_activa() {
        Cuenta cuenta = new Cuenta(1L, TipoCuenta.AHORROS, "123", EstadoCuenta.ACTIVA,
                BigDecimal.ZERO, false, LocalDateTime.now(), LocalDateTime.now(), 1L);
        assertDoesNotThrow(() -> CuentaValidator.validarCuentaActiva(cuenta));
    }

    @Test
    void deberia_rechazar_cuenta_inactiva() {
        Cuenta cuenta = new Cuenta(1L, TipoCuenta.AHORROS, "123", EstadoCuenta.INACTIVA,
                BigDecimal.ZERO, false, LocalDateTime.now(), LocalDateTime.now(), 1L);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> CuentaValidator.validarCuentaActiva(cuenta));
        assertEquals("La cuenta no está activa", ex.getMessage());
    }

    @Test
    void deberia_rechazar_cuenta_cancelada() {
        Cuenta cuenta = new Cuenta(1L, TipoCuenta.AHORROS, "123", EstadoCuenta.CANCELADA,
                BigDecimal.ZERO, false, LocalDateTime.now(), LocalDateTime.now(), 1L);
        RuntimeException ex = assertThrows(RuntimeException.class, () -> CuentaValidator.validarCuentaActiva(cuenta));
        assertEquals("La cuenta no está activa", ex.getMessage());
    }

    @Test
    void deberia_aceptar_saldo_disponible_en_ahorros() {
        Cuenta cuenta = new Cuenta(1L, TipoCuenta.AHORROS, "123", EstadoCuenta.ACTIVA,
                new BigDecimal("500"), false, LocalDateTime.now(), LocalDateTime.now(), 1L);
        assertDoesNotThrow(() -> CuentaValidator.validarSaldoDisponible(cuenta, new BigDecimal("300")));
    }

    @Test
    void deberia_rechazar_saldo_insuficiente_en_ahorros() {
        Cuenta cuenta = new Cuenta(1L, TipoCuenta.AHORROS, "123", EstadoCuenta.ACTIVA,
                new BigDecimal("100"), false, LocalDateTime.now(), LocalDateTime.now(), 1L);
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                CuentaValidator.validarSaldoDisponible(cuenta, new BigDecimal("200"))
        );
        assertEquals("Cuenta de ahorros no puede quedar en saldo negativo", ex.getMessage());
    }

    @Test
    void deberia_aceptar_saldo_exacto_cero_en_ahorros() {
        Cuenta cuenta = new Cuenta(1L, TipoCuenta.AHORROS, "123", EstadoCuenta.ACTIVA,
                new BigDecimal("100"), false, LocalDateTime.now(), LocalDateTime.now(), 1L);
        assertDoesNotThrow(() -> CuentaValidator.validarSaldoDisponible(cuenta, new BigDecimal("100")));
    }
}
