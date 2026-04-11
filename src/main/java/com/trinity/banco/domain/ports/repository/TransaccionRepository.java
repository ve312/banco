package com.trinity.banco.domain.ports.repository;

import com.trinity.banco.domain.model.Transaccion;

import java.util.List;

public interface TransaccionRepository {
    Transaccion guardar(Transaccion transaccion);

    List<Transaccion> listarPorNumeroCuenta(String numeroCuenta);
}
