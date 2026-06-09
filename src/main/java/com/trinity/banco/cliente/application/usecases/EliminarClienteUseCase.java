package com.trinity.banco.cliente.application.usecases;

import com.trinity.banco.cliente.domain.ports.ClienteRepository;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import com.trinity.banco.shared.domain.errors.RecursoNoEncontradoException;

public class EliminarClienteUseCase {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;

    public EliminarClienteUseCase(ClienteRepository clienteRepository,
                                  CuentaRepository cuentaRepository) {
        this.clienteRepository = clienteRepository;
        this.cuentaRepository = cuentaRepository;
    }

    public void ejecutar(Long clienteId) {

        if (!clienteRepository.existePorId(clienteId)) {
            throw new RecursoNoEncontradoException("Cliente no encontrado");
        }

        boolean tieneCuentas = cuentaRepository.existePorClienteId(clienteId);

        if (tieneCuentas) {
            throw new RuntimeException("No se puede eliminar el cliente porque tiene cuentas asociadas");
        }

        clienteRepository.eliminar(clienteId);
    }
}
