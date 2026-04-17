package com.trinity.banco.application.service.cuenta;

import com.trinity.banco.domain.model.Cuenta;
import com.trinity.banco.domain.ports.repository.CuentaRepository;
import com.trinity.banco.rest.exceptions.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;

@Service
public class ObtenerCuentaService {
    private final CuentaRepository cuentaRepository;

    public ObtenerCuentaService(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public Cuenta ejecutar(String numeroCuenta) {
        return cuentaRepository.buscarPorNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada"));
    }
}
