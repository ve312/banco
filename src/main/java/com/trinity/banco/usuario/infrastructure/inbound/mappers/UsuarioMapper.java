package com.trinity.banco.usuario.infrastructure.inbound.mappers;

import com.trinity.banco.usuario.domain.model.Usuario;
import com.trinity.banco.usuario.domain.model.enums.Rol;
import com.trinity.banco.usuario.infrastructure.inbound.dto.response.UsuarioResponse;

public class UsuarioMapper {

    private UsuarioMapper() {}

    public static Rol toRol(String rol) {
        try {
            return Rol.valueOf(rol.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException("Rol inválido: " + rol + ". Los roles válidos son: ADMIN, ASESOR, AUDITOR");
        }
    }

    public static UsuarioResponse toResponse(Usuario usuario) {
        if (usuario == null) return null;

        UsuarioResponse response = new UsuarioResponse();
        response.setId(usuario.getId());
        response.setUsername(usuario.getUsername());
        response.setNombre(usuario.getNombre());
        response.setApellido(usuario.getApellido());
        response.setActivo(usuario.isActivo());
        response.setRol(usuario.getRol().name());
        response.setFechaCreacion(usuario.getFechaCreacion());
        response.setFechaModificacion(usuario.getFechaModificacion());

        return response;
    }
}
