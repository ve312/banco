package com.trinity.banco.application.service.cliente;

import com.trinity.banco.application.validator.ClienteValidator;
import com.trinity.banco.domain.model.Cliente;
import com.trinity.banco.domain.ports.repository.ClienteRepository;
import com.trinity.banco.rest.exceptions.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;

@Service
public class ActualizarClienteService {
    private final ClienteRepository clienteRepository;

    public ActualizarClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente ejecutar(Long id, String nombres, String apellidos, String email) {

        Cliente cliente = clienteRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado"));

        ClienteValidator.validarEmail(email);
        ClienteValidator.validarNombre(nombres, apellidos);

        cliente.actualizarDatos(nombres, apellidos, email);

        return clienteRepository.guardar(cliente);
    }
}
