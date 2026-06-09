package com.trinity.banco.transaccion.infrastructure.inbound.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Solicitud para realizar un movimiento (consignación o retiro) en una cuenta")
public class MovimientoRequest {
    @NotBlank(message = "El número de cuenta es obligatorio")
    @Schema(description = "Número de cuenta afectada", example = "1000000001")
    private String numeroCuenta;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    @Schema(description = "Monto del movimiento", example = "150000.00")
    private BigDecimal monto;
}
