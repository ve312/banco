package com.trinity.banco.transaccion.application.usecases;

import com.trinity.banco.transaccion.domain.model.Transaccion;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import com.trinity.banco.transaccion.domain.ports.TransaccionRepository;
import com.trinity.banco.shared.errors.RecursoNoEncontradoException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarTransaccionesPorCuentaService {
    private final TransaccionRepository transaccionRepository;
    private final CuentaRepository cuentaRepository;

    public ListarTransaccionesPorCuentaService(TransaccionRepository transaccionRepository, CuentaRepository cuentaRepository) {
        this.transaccionRepository = transaccionRepository;
        this.cuentaRepository = cuentaRepository;
    }

    public List<Transaccion> ejecutar(String numeroCuenta) {

        if (numeroCuenta == null || numeroCuenta.isBlank()) {
            throw new RuntimeException("El número de cuenta es obligatorio");
        }

        cuentaRepository.buscarPorNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada"));

        return transaccionRepository.listarPorNumeroCuenta(numeroCuenta);
    }
}
