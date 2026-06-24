package com.trinity.banco.transaccion.domain.ports;

import com.trinity.banco.transaccion.domain.model.Transaccion;

import java.util.List;

public interface TransaccionRepository {
    Transaccion guardar(Transaccion transaccion);

    List<Transaccion> listarPorNumeroCuenta(String numeroCuenta);

    List<Transaccion> listarTodas();
}
