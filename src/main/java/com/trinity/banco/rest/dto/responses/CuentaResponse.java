package com.trinity.banco.rest.dto;

import com.trinity.banco.domain.model.enums.EstadoCuenta;
import com.trinity.banco.domain.model.enums.TipoCuenta;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class CuentaResponse {
    private Long id;
    private String tipoCuenta;
    private String numeroCuenta;
    private String estado;
    private BigDecimal saldo;
    private boolean exentaGMF;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private Long clienteId;
}
