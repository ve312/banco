package com.trinity.banco.cliente.infrastructure.inbound.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@Schema(description = "Datos completos de un cliente")
public class ClienteResponse {
    @Schema(description = "ID único del cliente", example = "1")
    private Long id;
    @Schema(description = "Tipo de identificación", example = "CC")
    private String tipoIdentificacion;
    @Schema(description = "Número de identificación", example = "1234567890")
    private String numeroIdentificacion;
    @Schema(description = "Nombres del cliente", example = "Juan Carlos")
    private String nombres;
    @Schema(description = "Apellidos del cliente", example = "Pérez López")
    private String apellidos;
    @Schema(description = "Correo electrónico", example = "juan.perez@email.com")
    private String email;
    @Schema(description = "Fecha de nacimiento", example = "1990-05-15")
    private LocalDate fechaNacimiento;
    @Schema(description = "Fecha de creación del registro")
    private LocalDateTime fechaCreacion;
    @Schema(description = "Fecha de última modificación")
    private LocalDateTime fechaModificacion;
}
