package com.trinity.banco.domain.model;

import com.trinity.banco.domain.model.enums.TipoTransaccion;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Transaccion {
    private final Long id;
    private final String numeroCuenta;
    private final TipoTransaccion tipoTransaccion;
    private final BigDecimal monto;
    private final BigDecimal saldoAnterior;
    private final BigDecimal saldoPosterior;
    private final LocalDateTime fecha;
    private final String numeroCuentaRelacionada;

    public Transaccion(Long id,
                       String numeroCuenta,
                       TipoTransaccion tipoTransaccion,
                       BigDecimal monto,
                       BigDecimal saldoAnterior,
                       BigDecimal saldoPosterior,
                       LocalDateTime fecha,
                       String numeroCuentaRelacionada) {

        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            throw new RuntimeException("El número de cuenta es obligatorio");
        }

        if (tipoTransaccion == null) {
            throw new RuntimeException("El tipo de transacción es obligatorio");
        }

        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("El monto debe ser mayor a cero");
        }

        if (saldoAnterior == null || saldoPosterior == null) {
            throw new RuntimeException("Los saldos son obligatorios");
        }

        if (fecha == null) {
            throw new RuntimeException("La fecha es obligatoria");
        }

        if (tipoTransaccion == TipoTransaccion.TRANSFERENCIA &&
                (numeroCuentaRelacionada == null || numeroCuentaRelacionada.isBlank())) {
            throw new RuntimeException("La cuenta relacionada es obligatoria para transferencias");
        }

        this.id = id;
        this.numeroCuenta = numeroCuenta;
        this.tipoTransaccion = tipoTransaccion;
        this.monto = monto;
        this.saldoAnterior = saldoAnterior;
        this.saldoPosterior = saldoPosterior;
        this.fecha = fecha;
        this.numeroCuentaRelacionada = numeroCuentaRelacionada;
    }
}
