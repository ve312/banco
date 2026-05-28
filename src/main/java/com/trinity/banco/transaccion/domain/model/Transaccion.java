package com.trinity.banco.transaccion.domain.model;

import com.trinity.banco.transaccion.domain.model.enums.TipoTransaccion;
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
    private final BigDecimal impuesto;

    public Transaccion(Long id,
                       String numeroCuenta,
                       TipoTransaccion tipoTransaccion,
                       BigDecimal monto,
                       BigDecimal saldoAnterior,
                       BigDecimal saldoPosterior,
                       LocalDateTime fecha,
                       String numeroCuentaRelacionada) {
        this(id, numeroCuenta, tipoTransaccion, monto, saldoAnterior, saldoPosterior,
                fecha, numeroCuentaRelacionada, BigDecimal.ZERO);
    }

    public Transaccion(Long id,
                       String numeroCuenta,
                       TipoTransaccion tipoTransaccion,
                       BigDecimal monto,
                       BigDecimal saldoAnterior,
                       BigDecimal saldoPosterior,
                       LocalDateTime fecha,
                       String numeroCuentaRelacionada,
                       BigDecimal impuesto) {

        this.id = id;
        this.numeroCuenta = numeroCuenta;
        this.tipoTransaccion = tipoTransaccion;
        this.monto = monto;
        this.saldoAnterior = saldoAnterior;
        this.saldoPosterior = saldoPosterior;
        this.fecha = fecha;
        this.numeroCuentaRelacionada = numeroCuentaRelacionada;
        this.impuesto = impuesto;
    }
}
