package com.trinity.banco.transaccion.infrastructure.outbound.mappers;

import com.trinity.banco.transaccion.domain.model.Transaccion;
import com.trinity.banco.infrastructure.entity.TransaccionEntity;

public class TransaccionMapper {
    public static TransaccionEntity toEntity(Transaccion transaccion) {
        TransaccionEntity entity = new TransaccionEntity();

        entity.setNumeroCuenta(transaccion.getNumeroCuenta());
        entity.setTipoTransaccion(transaccion.getTipoTransaccion());
        entity.setMonto(transaccion.getMonto());
        entity.setSaldoAnterior(transaccion.getSaldoAnterior());
        entity.setSaldoPosterior(transaccion.getSaldoPosterior());
        entity.setFecha(transaccion.getFecha());
        entity.setNumeroCuentaRelacionada(transaccion.getNumeroCuentaRelacionada());

        return entity;
    }

    public static Transaccion toDomain(TransaccionEntity entity) {
        return new Transaccion(
                entity.getId(),
                entity.getNumeroCuenta(),
                entity.getTipoTransaccion(),
                entity.getMonto(),
                entity.getSaldoAnterior(),
                entity.getSaldoPosterior(),
                entity.getFecha(),
                entity.getNumeroCuentaRelacionada()
        );
    }
}
