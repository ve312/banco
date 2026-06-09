package com.trinity.banco.cliente.application.usecases;

import com.trinity.banco.cliente.domain.model.Cliente;
import com.trinity.banco.cliente.domain.ports.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

public class ListarClientesUseCase {
    private final ClienteRepository clienteRepository;

    public ListarClientesUseCase(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> ejecutar() {
        return clienteRepository.listar();
    }
}
