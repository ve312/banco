package com.trinity.banco.cliente.infrastructure.inbound.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CrearClienteRequest {
    @NotBlank(message = "El tipo de identificación es obligatorio")
    private String tipoIdentificacion;

    @NotBlank(message = "El número de identificación es obligatorio")
    @Pattern(regexp = "\\d+", message = "El número de identificación solo debe contener números")
    @Size(min = 6, message = "El minimo de digitos son 6")
    @Size(max = 10, message = "El maximo de digitos son 10")
    private String numeroIdentificacion;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El nombre solo debe contener letras")
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, message = "El apellido debe tener al menos 2 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$", message = "El apellido solo debe contener letras")
    private String apellidos;

    @Email(message = "El correo debe tener un formato válido")
    @NotBlank(message = "El correo es obligatorio")
    private String email;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fechaNacimiento;
}
