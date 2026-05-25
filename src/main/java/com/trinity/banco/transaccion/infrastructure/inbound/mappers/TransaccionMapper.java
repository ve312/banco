package com.trinity.banco.rest.mapper;

import com.trinity.banco.transaccion.domain.model.Transaccion;
import com.trinity.banco.rest.dto.responses.TransaccionResponse;

public class TransaccionMapper {
    private TransaccionMapper() {
    }

    public static TransaccionResponse toResponse(Transaccion transaccion) {

        if (transaccion == null) {
            return null;
        }

        TransaccionResponse response = new TransaccionResponse();

        response.setId(transaccion.getId());
        response.setNumeroCuenta(transaccion.getNumeroCuenta());
        response.setTipoTransaccion(transaccion.getTipoTransaccion().name());
        response.setMonto(transaccion.getMonto());
        response.setSaldoAnterior(transaccion.getSaldoAnterior());
        response.setSaldoPosterior(transaccion.getSaldoPosterior());
        response.setFecha(transaccion.getFecha());
        response.setNumeroCuentaRelacionada(transaccion.getNumeroCuentaRelacionada());

        return response;
    }
}
