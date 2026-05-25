package com.trinity.banco.cuenta.application.usecases;

import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cuenta.domain.model.enums.EstadoCuenta;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import com.trinity.banco.shared.errors.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ActivarCuentaService {
    private final CuentaRepository cuentaRepository;

    public ActivarCuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public void ejecutar(String numeroCuenta) {

        Cuenta cuenta = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada"));

        if (cuenta.getEstado() == EstadoCuenta.ACTIVA) {
            throw new RuntimeException("La cuenta ya está activa");
        }

        cuenta.setEstado(EstadoCuenta.ACTIVA);
        cuenta.setFechaModificacion(LocalDateTime.now());

        cuentaRepository.guardar(cuenta);
    }
}
