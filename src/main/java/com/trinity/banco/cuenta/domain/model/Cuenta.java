package com.trinity.banco.cuenta.domain.model;

import com.trinity.banco.cuenta.domain.model.enums.EstadoCuenta;
import com.trinity.banco.cuenta.domain.model.enums.TipoCuenta;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class Cuenta {
    private static final BigDecimal LIMITE_GMF_MENSUAL = new BigDecimal("18330900");

    private Long id;
    private TipoCuenta tipoCuenta;
    private String numeroCuenta;
    @Setter
    private EstadoCuenta estado;
    @Setter
    private BigDecimal saldo;
    private boolean exentaGMF;
    private LocalDateTime fechaCreacion;
    @Setter
    private LocalDateTime fechaModificacion;
    private Long clienteId;
    @Setter
    private BigDecimal gmfAcumuladoMensual;
    @Setter
    private Integer mesAcumuladoGMF;

    public Cuenta(Long id,
                  TipoCuenta tipoCuenta,
                  String numeroCuenta,
                  EstadoCuenta estado,
                  BigDecimal saldo,
                  boolean exentaGMF,
                  LocalDateTime fechaCreacion,
                  LocalDateTime fechaModificacion,
                  Long clienteId) {
        this(id, tipoCuenta, numeroCuenta, estado, saldo, exentaGMF,
                fechaCreacion, fechaModificacion, clienteId,
                BigDecimal.ZERO, 0);
    }

    public Cuenta(Long id,
                  TipoCuenta tipoCuenta,
                  String numeroCuenta,
                  EstadoCuenta estado,
                  BigDecimal saldo,
                  boolean exentaGMF,
                  LocalDateTime fechaCreacion,
                  LocalDateTime fechaModificacion,
                  Long clienteId,
                  BigDecimal gmfAcumuladoMensual,
                  Integer mesAcumuladoGMF) {

        this.id = id;
        this.tipoCuenta = tipoCuenta;
        this.numeroCuenta = numeroCuenta;
        this.estado = estado;
        this.saldo = saldo;
        this.exentaGMF = exentaGMF;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
        this.clienteId = clienteId;
        this.gmfAcumuladoMensual = gmfAcumuladoMensual;
        this.mesAcumuladoGMF = mesAcumuladoGMF;
    }

    public BigDecimal getLimiteGmfMensual() {
        return LIMITE_GMF_MENSUAL;
    }

}
