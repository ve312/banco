package com.trinity.banco.cuenta.application.usecases;

import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cuenta.domain.model.enums.EstadoCuenta;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import com.trinity.banco.shared.domain.errors.RecursoNoEncontradoException;

import java.time.LocalDateTime;

public class InactivarCuentaUseCase {
    private final CuentaRepository cuentaRepository;

    public InactivarCuentaUseCase(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public void ejecutar(String numeroCuenta) {

        Cuenta cuenta = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada"));

        if (cuenta.getEstado() == EstadoCuenta.INACTIVA) {
            throw new RuntimeException("La cuenta ya está inactiva");
        }

        cuenta.setEstado(EstadoCuenta.INACTIVA);
        cuenta.setFechaModificacion(LocalDateTime.now());

        cuentaRepository.guardar(cuenta);
    }
}
