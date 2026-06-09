package com.trinity.banco.cliente.domain.ports;

import com.trinity.banco.cliente.domain.model.Cliente;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository {
    Cliente guardar(Cliente cliente);

    Optional<Cliente> buscarPorId(Long id);

    List<Cliente> listar();

    void eliminar(Long id);

    boolean existePorNumeroIdentificacion(String numeroIdentificacion);

    boolean existePorId(Long id);

    boolean existePorEmail(String email);
}
