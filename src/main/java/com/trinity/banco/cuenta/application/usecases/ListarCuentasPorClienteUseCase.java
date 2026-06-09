package com.trinity.banco.cuenta.application.usecases;

import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cliente.domain.ports.ClienteRepository;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import com.trinity.banco.shared.domain.errors.RecursoNoEncontradoException;

import java.util.List;

public class ListarCuentasPorClienteUseCase {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    public ListarCuentasPorClienteUseCase(CuentaRepository cuentaRepository,
                                          ClienteRepository clienteRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Cuenta> ejecutar(Long clienteId) {

        if (!clienteRepository.existePorId(clienteId)) {
            throw new RecursoNoEncontradoException("Cliente no encontrado");
        }

        return cuentaRepository.listarPorClienteId(clienteId);
    }
}
