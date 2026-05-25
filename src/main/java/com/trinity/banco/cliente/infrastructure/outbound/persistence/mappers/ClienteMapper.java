package com.trinity.banco.cliente.infrastructure.outbound.persistence.mappers;

import com.trinity.banco.cliente.domain.model.Cliente;
import com.trinity.banco.cliente.infrastructure.outbound.persistence.entity.ClienteEntity;

public class ClienteMapper {
    public static Cliente toDomain(ClienteEntity entity) {
        return new Cliente(
                entity.getId(),
                entity.getTipoIdentificacion(),
                entity.getNumeroIdentificacion(),
                entity.getNombres(),
                entity.getApellidos(),
                entity.getEmail(),
                entity.getFechaNacimiento(),
                entity.getFechaCreacion(),
                entity.getFechaModificacion()
        );
    }

    public static ClienteEntity toEntity(Cliente cliente) {
        return new ClienteEntity(
                cliente.getId(),
                cliente.getTipoIdentificacion(),
                cliente.getNumeroIdentificacion(),
                cliente.getNombres(),
                cliente.getApellidos(),
                cliente.getEmail(),
                cliente.getFechaNacimiento(),
                cliente.getFechaCreacion(),
                cliente.getFechaModificacion()
        );
    }
}
