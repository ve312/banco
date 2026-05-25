package com.trinity.banco.infrastructure.entity;

import com.trinity.banco.cuenta.domain.model.enums.EstadoCuenta;
import com.trinity.banco.cuenta.domain.model.enums.TipoCuenta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cuentas")
@Getter
@Setter
public class CuentaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoCuenta tipoCuenta;

    @Column(unique = true, nullable = false, length = 10)
    private String numeroCuenta;

    @Enumerated(EnumType.STRING)
    private EstadoCuenta estado;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    private boolean exentaGMF;

    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;

    private Long clienteId;

    public CuentaEntity() {}

    public CuentaEntity(Long id,
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
}
