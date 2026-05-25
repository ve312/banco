package com.trinity.banco.cuenta.application.validators;

import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cuenta.domain.model.enums.EstadoCuenta;
import com.trinity.banco.cuenta.domain.model.enums.TipoCuenta;

import java.math.BigDecimal;

public class CuentaValidator {
    private CuentaValidator() {
    }

    public static void validarTipoCuenta(TipoCuenta tipoCuenta) {
        if (tipoCuenta == null) {
            throw new RuntimeException("El tipo de cuenta es obligatorio");
        }
    }

    public static void validarSaldoInicial(TipoCuenta tipoCuenta, BigDecimal saldoInicial) {
        if (saldoInicial == null) {
            throw new RuntimeException("El saldo inicial es obligatorio");
        }

        if (tipoCuenta == TipoCuenta.AHORROS && saldoInicial.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("La cuenta de ahorros no puede iniciar con saldo negativo");
        }
    }

    public static void validarMonto(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El monto debe ser mayor a cero");
        }
    }

    public static void validarCuentaActiva(Cuenta cuenta) {
        if (cuenta.getEstado() != EstadoCuenta.ACTIVA) {
            throw new RuntimeException("La cuenta no está activa");
        }
    }

    public static void validarSaldoDisponible(Cuenta cuenta, BigDecimal monto) {
        if (cuenta.getTipoCuenta() == TipoCuenta.AHORROS
                && cuenta.getSaldo().subtract(monto).compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Cuenta de ahorros no puede quedar en saldo negativo");
        }
    }
}
