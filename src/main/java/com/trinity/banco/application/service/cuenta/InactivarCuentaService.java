package com.trinity.banco.application.service.cuenta;

import com.trinity.banco.domain.model.Cuenta;
import com.trinity.banco.domain.ports.repository.CuentaRepository;
import org.springframework.stereotype.Service;

@Service
public class InactivarCuentaService {
    private final CuentaRepository cuentaRepository;

    public InactivarCuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public void ejecutar(String numeroCuenta) {

        Cuenta cuenta = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        cuenta.inactivar();

        cuentaRepository.guardar(cuenta);
    }
}
