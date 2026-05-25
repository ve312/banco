package com.trinity.banco.cliente.infrastructure.inbound.mappers;

import com.trinity.banco.cliente.domain.model.Cliente;
import com.trinity.banco.cliente.domain.model.enums.TipoIdentificacion;
import com.trinity.banco.cliente.infrastructure.inbound.dto.response.ClienteResponse;

public class ClienteMapper {

    private ClienteMapper() {
    }

    public static TipoIdentificacion toTipoIdentificacion(String tipo) {
        try {
            return TipoIdentificacion.valueOf(tipo.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException("Tipo de identificación inválido: " + tipo);
        }
    }

    public static ClienteResponse toResponse(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        ClienteResponse response = new ClienteResponse();

        response.setId(cliente.getId());
        response.setTipoIdentificacion(cliente.getTipoIdentificacion().name());
        response.setNumeroIdentificacion(cliente.getNumeroIdentificacion());
        response.setNombres(cliente.getNombres());
        response.setApellidos(cliente.getApellidos());
        response.setEmail(cliente.getEmail());
        response.setFechaNacimiento(cliente.getFechaNacimiento());
        response.setFechaCreacion(cliente.getFechaCreacion());
        response.setFechaModificacion(cliente.getFechaModificacion());

        return response;
    }
}
