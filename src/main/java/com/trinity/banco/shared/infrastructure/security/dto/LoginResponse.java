package com.trinity.banco.shared.infrastructure.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Respuesta de inicio de sesión con JWT")
public class LoginResponse {
    @Schema(description = "Token JWT para autenticación", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String token;

    @Schema(description = "Tipo de token", example = "Bearer")
    private String tipo;

    @Schema(description = "Username del usuario autenticado", example = "admin")
    private String username;

    @Schema(description = "Rol del usuario", example = "ADMIN")
    private String rol;

    public LoginResponse(String token, String username, String rol) {
        this.token = token;
        this.tipo = "Bearer";
        this.username = username;
        this.rol = rol;
    }
}
