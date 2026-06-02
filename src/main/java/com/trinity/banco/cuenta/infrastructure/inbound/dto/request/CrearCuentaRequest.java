package com.trinity.banco.cuenta.infrastructure.inbound.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Solicitud para crear una nueva cuenta bancaria")
public class CrearCuentaRequest {
    @NotBlank(message = "El tipo de cuenta es obligatorio")
    @Schema(description = "Tipo de cuenta (AHORROS o CORRIENTE)", example = "AHORROS")
    private String tipoCuenta;

    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El saldo inicial no puede ser negativo")
    @Schema(description = "Saldo inicial de la cuenta", example = "500000.00")
    private BigDecimal saldoInicial;

    @Schema(description = "Indica si la cuenta está exenta del Gravamen a los Movimientos Financieros (GMF)", example = "false")
    private boolean exentaGMF;

    @NotNull(message = "El cliente es obligatorio")
    @Schema(description = "ID del cliente titular de la cuenta", example = "1")
    private Long clienteId;

}
