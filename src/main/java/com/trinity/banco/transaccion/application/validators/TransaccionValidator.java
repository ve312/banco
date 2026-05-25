package com.trinity.banco.transaccion.application.validators;

import com.trinity.banco.transaccion.domain.model.enums.TipoTransaccion;

import java.math.BigDecimal;

public class TransaccionValidator {
    private TransaccionValidator() {
    }

    public static void validarNumeroCuenta(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            throw new RuntimeException("El número de cuenta es obligatorio");
        }
    }

    public static void validarTipoTransaccion(TipoTransaccion tipoTransaccion) {
        if (tipoTransaccion == null) {
            throw new RuntimeException("El tipo de transacción es obligatorio");
        }
    }

    public static void validarMonto(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El monto debe ser mayor a cero");
        }
    }

    public static void validarSaldos(BigDecimal saldoAnterior, BigDecimal saldoPosterior) {
        if (saldoAnterior == null || saldoPosterior == null) {
            throw new RuntimeException("Los saldos son obligatorios");
        }
    }

    public static void validarFecha(Object fecha) {
        if (fecha == null) {
            throw new RuntimeException("La fecha es obligatoria");
        }
    }

    public static void validarCuentaRelacionada(TipoTransaccion tipoTransaccion, String numeroCuentaRelacionada) {
        if (tipoTransaccion == TipoTransaccion.TRANSFERENCIA &&
                (numeroCuentaRelacionada == null || numeroCuentaRelacionada.isBlank())) {
            throw new RuntimeException("La cuenta relacionada es obligatoria para transferencias");
        }
    }
}
