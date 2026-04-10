package com.trinity.banco.domain.model;

import com.trinity.banco.domain.model.enums.EstadoCuenta;
import com.trinity.banco.domain.model.enums.TipoCuenta;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Cuenta {
    private Long id;
    private TipoCuenta tipoCuenta;
    private String numeroCuenta;
    private EstadoCuenta estado;
    private BigDecimal saldo;
    private boolean exentaGMF;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private Long clienteId;

    public Cuenta(Long id,
                  TipoCuenta tipoCuenta,
                  String numeroCuenta,
                  EstadoCuenta estado,
                  BigDecimal saldo,
                  boolean exentaGMF,
                  LocalDateTime fechaCreacion,
                  LocalDateTime fechaModificacion,
                  Long clienteId) {

        this.id = id;
        this.tipoCuenta = tipoCuenta;
        this.numeroCuenta = numeroCuenta;
        this.estado = estado;
        this.saldo = saldo;
        this.exentaGMF = exentaGMF;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.clienteId = clienteId;
    }


    public void activar() {
        if (this.estado == EstadoCuenta.ACTIVA) {
            throw new RuntimeException("La cuenta ya está activa");
        }
        this.estado = EstadoCuenta.ACTIVA;
        this.fechaModificacion = LocalDateTime.now();
    }

    public void inactivar() {
        if (this.estado == EstadoCuenta.INACTIVA) {
            throw new RuntimeException("La cuenta ya está inactiva");
        }
        this.estado = EstadoCuenta.INACTIVA;
        this.fechaModificacion = LocalDateTime.now();
    }

    public void cancelar() {
        if (this.saldo.compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("No se puede cancelar una cuenta con saldo diferente a 0");
        }
        this.estado = EstadoCuenta.CANCELADA;
        this.fechaModificacion = LocalDateTime.now();
    }

    public void depositar(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Monto inválido");
        }
        this.saldo = this.saldo.add(monto);
        this.fechaModificacion = LocalDateTime.now();
    }

    public void retirar(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Monto inválido");
        }
        if (this.esAhorros() && this.saldo.subtract(monto).compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Cuenta de ahorros no puede quedar en saldo negativo");
        }
        this.saldo = this.saldo.subtract(monto);
        this.fechaModificacion = LocalDateTime.now();
    }

    public boolean esAhorros() {
        return this.tipoCuenta == TipoCuenta.AHORROS;
    }
}
