package com.trinity.banco.application.service.cliente;

import com.trinity.banco.domain.model.Cliente;
import com.trinity.banco.domain.ports.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarClientesService {
    private final ClienteRepository clienteRepository;

    public ListarClientesService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> ejecutar() {
        return clienteRepository.listar();
    }
}
