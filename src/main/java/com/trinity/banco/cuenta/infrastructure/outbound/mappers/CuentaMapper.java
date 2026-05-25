package com.trinity.banco.cuenta.infrastructure.outbound.mappers;

import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cuenta.domain.model.enums.TipoCuenta;
import com.trinity.banco.cuenta.infrastructure.inbound.dto.response.CuentaResponse;

public class CuentaMapper {
    private CuentaMapper() {
    }


    public static TipoCuenta toTipoCuenta(String tipoCuenta) {
        try {
            return TipoCuenta.valueOf(tipoCuenta.toUpperCase());
        } catch (Exception e) {
            throw new RuntimeException("Tipo de cuenta inválido: " + tipoCuenta);
        }
    }


    public static CuentaResponse toResponse(Cuenta cuenta) {
        if (cuenta == null) {
            return null;
        }

        CuentaResponse response = new CuentaResponse();

        response.setId(cuenta.getId());
        response.setTipoCuenta(cuenta.getTipoCuenta().name());
        response.setNumeroCuenta(cuenta.getNumeroCuenta());
        response.setEstado(cuenta.getEstado().name());
        response.setSaldo(cuenta.getSaldo());
        response.setExentaGMF(cuenta.isExentaGMF());
        response.setFechaCreacion(cuenta.getFechaCreacion());
        response.setFechaModificacion(cuenta.getFechaModificacion());
        response.setClienteId(cuenta.getClienteId());

        return response;
    }
}
