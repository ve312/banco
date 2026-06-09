package com.trinity.banco.cliente.infrastructure.inbound.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Solicitud para crear un nuevo cliente")
public class CrearClienteRequest {
    @NotBlank(message = "El tipo de identificación es obligatorio")
    @Schema(description = "Tipo de identificación (CC, CE, NIT, etc.)", example = "CC")
    private String tipoIdentificacion;

    @NotBlank(message = "El número de identificación es obligatorio")
    @Pattern(regexp = "\\d+", message = "El número de identificación solo debe contener números")
    @Size(min = 6, message = "El minimo de digitos son 6")
    @Size(max = 10, message = "El maximo de digitos son 10")
    @Schema(description = "Número de identificación (6-10 dígitos)", example = "1234567890")
    private String numeroIdentificacion;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre solo debe contener letras")
    @Schema(description = "Nombres del cliente", example = "Juan Carlos")
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, message = "El apellido debe tener al menos 2 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El apellido solo debe contener letras")
    @Schema(description = "Apellidos del cliente", example = "Pérez López")
    private String apellidos;

    @Email(message = "El correo debe tener un formato válido")
    @NotBlank(message = "El correo es obligatorio")
    @Schema(description = "Correo electrónico del cliente", example = "juan.perez@email.com")
    private String email;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Schema(description = "Fecha de nacimiento del cliente", example = "1990-05-15")
    private LocalDate fechaNacimiento;
}
