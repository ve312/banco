package com.trinity.banco.application.service.cuenta;

import com.trinity.banco.domain.model.Cuenta;
import com.trinity.banco.domain.ports.repository.CuentaRepository;
import org.springframework.stereotype.Service;

@Service
public class ActivarCuentaService {
    private final CuentaRepository cuentaRepository;

    public ActivarCuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public void ejecutar(String numeroCuenta) {

        Cuenta cuenta = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        cuenta.activar();

        cuentaRepository.guardar(cuenta);
    }
}
