package com.trinity.banco.cliente.application.usecases;

import com.trinity.banco.cliente.domain.model.Cliente;
import com.trinity.banco.cliente.domain.ports.ClienteRepository;
import com.trinity.banco.shared.domain.errors.RecursoNoEncontradoException;

public class ObtenerClienteUseCase {
    private final ClienteRepository clienteRepository;

    public ObtenerClienteUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente ejecutar(Long id) {
        return clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));
    }

}
