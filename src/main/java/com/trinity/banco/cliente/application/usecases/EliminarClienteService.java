package com.trinity.banco.cliente.application.usecases;

import com.trinity.banco.cliente.domain.ports.ClienteRepository;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import com.trinity.banco.shared.errors.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;

@Service
public class EliminarClienteService {

    private final ClienteRepository clienteRepository;
    private final CuentaRepository cuentaRepository;

    public EliminarClienteService(ClienteRepository clienteRepository,
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
