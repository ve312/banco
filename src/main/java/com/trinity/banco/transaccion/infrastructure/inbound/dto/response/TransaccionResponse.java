package com.trinity.banco.transaccion.infrastructure.inbound.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Datos de una transacción bancaria")
public class TransaccionResponse {
    @Schema(description = "ID único de la transacción", example = "1")
    private Long id;
    @Schema(description = "Número de cuenta afectada", example = "1000000001")
    private String numeroCuenta;
    @Schema(description = "Tipo de transacción (CONSIGNACION, RETIRO, TRANSFERENCIA_ENVIO, TRANSFERENCIA_RECIBO)", example = "CONSIGNACION")
    private String tipoTransaccion;
    @Schema(description = "Monto de la transacción", example = "150000.00")
    private BigDecimal monto;
    @Schema(description = "Saldo de la cuenta antes de la transacción", example = "500000.00")
    private BigDecimal saldoAnterior;
    @Schema(description = "Saldo de la cuenta después de la transacción", example = "650000.00")
    private BigDecimal saldoPosterior;
    @Schema(description = "Fecha y hora de la transacción")
    private LocalDateTime fecha;
    @Schema(description = "Número de cuenta relacionada (para transferencias)", example = "1000000002")
    private String numeroCuentaRelacionada;
    @Schema(description = "Impuesto GMF aplicado a la transacción", example = "0.00")
    private BigDecimal impuesto;
}
