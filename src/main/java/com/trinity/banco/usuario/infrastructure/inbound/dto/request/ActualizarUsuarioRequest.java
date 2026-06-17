package com.trinity.banco.usuario.infrastructure.inbound.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Solicitud para actualizar un usuario existente")
public class ActualizarUsuarioRequest {
    @NotBlank(message = "El nombre es obligatorio")
    @Schema(description = "Nombres del empleado", example = "Juan Carlos")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Schema(description = "Apellidos del empleado", example = "Pérez López")
    private String apellido;

    @NotBlank(message = "El rol es obligatorio")
    @Schema(description = "Rol del usuario (ADMIN, ASESOR, AUDITOR)", example = "ASESOR")
    private String rol;

    @Schema(description = "Estado del usuario (activo/inactivo)", example = "true")
    private boolean activo;
}
