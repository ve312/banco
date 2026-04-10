package com.trinity.banco.application.service.cliente;

import com.trinity.banco.domain.model.Cliente;
import com.trinity.banco.domain.ports.repository.ClienteRepository;
import com.trinity.banco.domain.ports.repository.CuentaRepository;
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
            throw new RuntimeException("Cliente no encontrado");
        }

        boolean tieneCuentas = cuentaRepository.existePorClienteId(clienteId);

        if (tieneCuentas) {
            throw new RuntimeException("No se puede eliminar el cliente porque tiene cuentas asociadas");
        }

        clienteRepository.eliminar(clienteId);
    }
}
