package com.trinity.banco.usuario.infrastructure.outbound.persistence.mappers;

import com.trinity.banco.usuario.domain.model.Usuario;
import com.trinity.banco.usuario.infrastructure.outbound.persistence.entity.UsuarioEntity;

public class UsuarioMapper {
    private UsuarioMapper() {}

    public static Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) return null;

        return new Usuario(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getNombre(),
                entity.getApellido(),
                entity.isActivo(),
                entity.getRol(),
                entity.getFechaCreacion(),
                entity.getFechaModificacion()
        );
    }

    public static UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) return null;

        return new UsuarioEntity(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getPassword(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.isActivo(),
                usuario.getRol(),
                usuario.getFechaCreacion(),
                usuario.getFechaModificacion()
        );
    }
}
