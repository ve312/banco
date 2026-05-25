package com.trinity.banco.cuenta.application.usecases;

import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cliente.domain.ports.ClienteRepository;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import com.trinity.banco.shared.errors.RecursoNoEncontradoException;
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
            throw new RecursoNoEncontradoException("Cliente no encontrado");
        }

        return cuentaRepository.listarPorClienteId(clienteId);
    }
}
