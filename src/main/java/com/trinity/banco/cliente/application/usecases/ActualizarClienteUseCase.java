package com.trinity.banco.cliente.application.usecases;

import com.trinity.banco.cliente.application.validators.ClienteValidator;
import com.trinity.banco.cliente.domain.model.Cliente;
import com.trinity.banco.cliente.domain.ports.ClienteRepository;
import com.trinity.banco.shared.domain.errors.RecursoNoEncontradoException;


public class ActualizarClienteUseCase {
    private final ClienteRepository clienteRepository;

    public ActualizarClienteUseCase(ClienteRepository clienteRepository) {
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
