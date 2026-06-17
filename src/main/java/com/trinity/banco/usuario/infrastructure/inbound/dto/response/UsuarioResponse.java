package com.trinity.banco.usuario.infrastructure.inbound.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "Datos completos de un usuario del sistema (sin contraseña)")
public class UsuarioResponse {
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre de usuario", example = "jperez")
    private String username;

    @Schema(description = "Nombres del empleado", example = "Juan Carlos")
    private String nombre;

    @Schema(description = "Apellidos del empleado", example = "Pérez López")
    private String apellido;

    @Schema(description = "Estado activo/inactivo", example = "true")
    private boolean activo;

    @Schema(description = "Rol del usuario", example = "ASESOR")
    private String rol;

    @Schema(description = "Fecha de creación del registro")
    private LocalDateTime fechaCreacion;

    @Schema(description = "Fecha de última modificación")
    private LocalDateTime fechaModificacion;
}
