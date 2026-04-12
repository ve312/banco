package com.trinity.banco.rest.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarClienteRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, message = "El nombre debe tener al menos 2 caracteres")
    private String nombres;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, message = "El apellido debe tener al menos 2 caracteres")
    private String apellidos;

    @Email(message = "El correo debe tener un formato válido")
    @NotBlank(message = "El correo es obligatorio")
    private String email;
}
