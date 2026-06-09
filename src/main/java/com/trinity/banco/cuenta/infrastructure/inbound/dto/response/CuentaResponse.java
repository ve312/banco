package com.trinity.banco.cuenta.infrastructure.inbound.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Datos de una cuenta bancaria")
public class CuentaResponse {
    @Schema(description = "ID único de la cuenta", example = "1")
    private Long id;
    @Schema(description = "Tipo de cuenta (AHORROS o CORRIENTE)", example = "AHORROS")
    private String tipoCuenta;
    @Schema(description = "Número único de la cuenta", example = "1000000001")
    private String numeroCuenta;
    @Schema(description = "Estado actual de la cuenta", example = "ACTIVA")
    private String estado;
    @Schema(description = "Saldo disponible", example = "500000.00")
    private BigDecimal saldo;
    @Schema(description = "Indica si la cuenta está exenta de GMF", example = "false")
    private boolean exentaGMF;
    @Schema(description = "Fecha de creación de la cuenta")
    private LocalDateTime fechaCreacion;
    @Schema(description = "Fecha de última modificación")
    private LocalDateTime fechaModificacion;
    @Schema(description = "ID del cliente titular", example = "1")
    private Long clienteId;
}
