package com.trinity.banco.cuenta.infrastructure.outbound.persistence.mappers;

import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cuenta.infrastructure.outbound.persistence.entity.CuentaEntity;

public class CuentaMapper {
    private CuentaMapper() {}

    public static Cuenta toDomain(CuentaEntity entity) {
        if (entity == null) return null;

        return new Cuenta(
                entity.getId(),
                entity.getTipoCuenta(),
                entity.getNumeroCuenta(),
                entity.getEstado(),
                entity.getSaldo(),
                entity.isExentaGMF(),
                entity.getFechaCreacion(),
                entity.getFechaModificacion(),
                entity.getClienteId()
        );
    }

    public static CuentaEntity toEntity(Cuenta domain) {
        if (domain == null) return null;

        return new CuentaEntity(
                domain.getId(),
                domain.getTipoCuenta(),
                domain.getNumeroCuenta(),
                domain.getEstado(),
                domain.getSaldo(),
                domain.isExentaGMF(),
                domain.getFechaCreacion(),
                domain.getFechaModificacion(),
                domain.getClienteId()
        );
    }
}
