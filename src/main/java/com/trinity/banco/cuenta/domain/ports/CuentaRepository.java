package com.trinity.banco.cuenta.domain.ports;

import com.trinity.banco.cuenta.domain.model.Cuenta;

import java.util.List;
import java.util.Optional;

public interface CuentaRepository {
    Cuenta guardar(Cuenta cuenta);

    Optional<Cuenta> buscarPorNumeroCuenta(String numeroCuenta);

    boolean existePorNumeroCuenta(String numeroCuenta);

    List<Cuenta> listarPorClienteId(Long clienteId);

    boolean existePorClienteId(Long clienteId);
}
