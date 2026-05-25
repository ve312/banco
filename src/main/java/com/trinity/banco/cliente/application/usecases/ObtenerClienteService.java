package com.trinity.banco.cliente.application.usecases;

import com.trinity.banco.cliente.domain.model.Cliente;
import com.trinity.banco.cliente.domain.ports.ClienteRepository;
import com.trinity.banco.shared.errors.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;

@Service
public class ObtenerClienteService {
    private final ClienteRepository clienteRepository;

    public ObtenerClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente ejecutar(Long id) {
        return clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
    }

}
