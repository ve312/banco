package com.trinity.banco.transaccion.infrastructure.inbound.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Schema(description = "Solicitud para realizar una transferencia entre cuentas")
public class TransferenciaRequest {
    @NotBlank(message = "La cuenta origen es obligatoria")
    @Schema(description = "Número de cuenta de origen", example = "1000000001")
    private String cuentaOrigenNumero;

    @NotBlank(message = "La cuenta destino es obligatoria")
    @Schema(description = "Número de cuenta de destino", example = "1000000002")
    private String cuentaDestinoNumero;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    @Schema(description = "Monto a transferir", example = "200000.00")
    private BigDecimal monto;
}
