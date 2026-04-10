package com.trinity.banco.application.service.cliente;

import com.trinity.banco.domain.model.Cliente;
import com.trinity.banco.domain.ports.repository.ClienteRepository;
import org.springframework.stereotype.Service;

@Service
public class ObtenerClienteService {
    private final ClienteRepository clienteRepository;

    public ObtenerClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente ejecutar(Long id) {
        return clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

}
