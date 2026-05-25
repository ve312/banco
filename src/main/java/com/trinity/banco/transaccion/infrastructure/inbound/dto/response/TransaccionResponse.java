package com.trinity.banco.transaccion.infrastructure.inbound.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class TransaccionResponse {
    private Long id;
    private String numeroCuenta;
    private String tipoTransaccion;
    private BigDecimal monto;
    private BigDecimal saldoAnterior;
    private BigDecimal saldoPosterior;
    private LocalDateTime fecha;
    private String numeroCuentaRelacionada;
}
