package com.trinity.banco.application.service.cuenta;

import com.trinity.banco.domain.model.Cuenta;
import com.trinity.banco.domain.ports.repository.CuentaRepository;
import com.trinity.banco.rest.exceptions.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;

@Service
public class CancelarCuentaService {
    private final CuentaRepository cuentaRepository;

    public CancelarCuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public void ejecutar(String numeroCuenta) {

        Cuenta cuenta = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada"));

        cuenta.cancelar();

        cuentaRepository.guardar(cuenta);
    }
}
