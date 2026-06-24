package com.trinity.banco.cuenta.application.usecases;

import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;

import java.util.List;

public class ListarTodasLasCuentasUseCase {
    private final CuentaRepository cuentaRepository;

    public ListarTodasLasCuentasUseCase(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public List<Cuenta> ejecutar() {
        return cuentaRepository.listarTodas();
    }
}
