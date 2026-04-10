package com.trinity.banco.application.service.cuenta;

import com.trinity.banco.domain.model.Cuenta;
import com.trinity.banco.domain.ports.repository.ClienteRepository;
import com.trinity.banco.domain.ports.repository.CuentaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarCuentasPorClienteService {

    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;

    public ListarCuentasPorClienteService(CuentaRepository cuentaRepository,
                                          ClienteRepository clienteRepository) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Cuenta> ejecutar(Long clienteId) {

        if (!clienteRepository.existePorId(clienteId)) {
            throw new RuntimeException("Cliente no encontrado");
        }

        return cuentaRepository.listarPorClienteId(clienteId);
    }
}
