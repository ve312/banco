package com.trinity.banco.cuenta.application.util;

import com.trinity.banco.cuenta.domain.model.enums.TipoCuenta;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;

import java.util.Random;

public class NumeroCuentaGenerator {
    private final CuentaRepository cuentaRepository;
    private final Random random = new Random();

    public NumeroCuentaGenerator(CuentaRepository cuentaRepository) {
        this.cuentaRepository = cuentaRepository;
    }

    public String generar(TipoCuenta tipoCuenta) {
        String prefijo = obtenerPrefijo(tipoCuenta);

        String numeroCuenta;
        int intentos = 0;

        do {
            numeroCuenta = prefijo + generarNumerosAleatorios(8);
            intentos++;

            if (intentos > 10) {
                throw new RuntimeException("No se pudo generar un número de cuenta único");
            }

        } while (cuentaRepository.existePorNumeroCuenta(numeroCuenta));

        return numeroCuenta;
    }

    private String obtenerPrefijo(TipoCuenta tipoCuenta) {
        return tipoCuenta == TipoCuenta.AHORROS ? "53" : "33";
    }

    private String generarNumerosAleatorios(int cantidad) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < cantidad; i++) {
            sb.append(random.nextInt(10));
        }

        return sb.toString();
    }
}
