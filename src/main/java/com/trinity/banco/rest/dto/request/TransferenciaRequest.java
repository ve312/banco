package com.trinity.banco.rest.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class TransferenciaRequest {
    @NotBlank(message = "La cuenta origen es obligatoria")
    private String cuentaOrigenNumero;

    @NotBlank(message = "La cuenta destino es obligatoria")
    private String cuentaDestinoNumero;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    private BigDecimal monto;
}
