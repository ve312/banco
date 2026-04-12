package com.trinity.banco.rest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CrearClienteRequest {
    @NotBlank(message = "El tipo de identificación es obligatorio")
    private String tipoIdentificacion;

    @NotBlank(message = "El número de identificación es obligatorio")
    private String numeroIdentificacion;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, message = "El apellido debe tener al menos 2 caracteres")
    private String apellidos;

    @Email(message = "El correo debe tener un formato válido")
    @NotBlank(message = "El correo es obligatorio")
    private String email;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate fechaNacimiento;
}
