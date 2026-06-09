package com.trinity.banco.cuenta.application.usecases;

import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cuenta.domain.model.enums.EstadoCuenta;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import com.trinity.banco.shared.domain.errors.RecursoNoEncontradoException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CancelarCuentaUseCase {
    private final CuentaRepository cuentaRepository;

    public CancelarCuentaUseCase(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public void ejecutar(String numeroCuenta) {

        Cuenta cuenta = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada"));

        if (cuenta.getEstado() == EstadoCuenta.CANCELADA) {
            throw new RuntimeException("La cuenta ya está cancelada");
        }

        if (cuenta.getSaldo().compareTo(BigDecimal.ZERO) != 0) {
            throw new RuntimeException("No se puede cancelar una cuenta con saldo diferente a 0");
        }

        cuenta.setEstado(EstadoCuenta.CANCELADA);
        cuenta.setFechaModificacion(LocalDateTime.now());

        cuentaRepository.guardar(cuenta);
    }
}
