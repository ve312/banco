package com.trinity.banco.usuario.infrastructure.inbound.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Solicitud para crear un nuevo usuario del sistema")
public class CrearUsuarioRequest {
    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, message = "El username debe tener al menos 3 caracteres")
    @Schema(description = "Nombre de usuario único para iniciar sesión", example = "jperez")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Schema(description = "Contraseña del usuario", example = "123456")
    private String password;

    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombres del empleado", example = "Juan")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Schema(description = "Apellidos del empleado", example = "Pérez")
    private String apellido;

    @NotBlank(message = "El rol es obligatorio")
    @Schema(description = "Rol del usuario (ADMIN, ASESOR, AUDITOR)", example = "ASESOR")
    private String rol;
}
