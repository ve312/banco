package com.trinity.banco.rest.mapper;

import com.trinity.banco.domain.model.Cliente;
import com.trinity.banco.domain.model.enums.TipoIdentificacion;
import com.trinity.banco.rest.dto.responses.ClienteResponse;
import com.trinity.banco.rest.dto.request.CrearClienteRequest;

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

    public static String toNumeroIdentificacion(CrearClienteRequest request) {
        return request.getNumeroIdentificacion();
    }

    public static String toNombres(CrearClienteRequest request) {
        return request.getNombres();
    }

    public static String toApellidos(CrearClienteRequest request) {
        return request.getApellidos();
    }

    public static String toEmail(CrearClienteRequest request) {
        return request.getEmail();
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
