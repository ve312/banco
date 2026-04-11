package com.trinity.banco.infrastructure.entity;

import com.trinity.banco.domain.model.enums.TipoTransaccion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacciones")
@Getter
@Setter
public class TransaccionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String numeroCuenta;

    @Enumerated(EnumType.STRING)
    private TipoTransaccion tipoTransaccion;

    private BigDecimal monto;

    private BigDecimal saldoAnterior;

    private BigDecimal saldoPosterior;

    private LocalDateTime fecha;

    private String numeroCuentaRelacionada;

    public TransaccionEntity() {}

}
