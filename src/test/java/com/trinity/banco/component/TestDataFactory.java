package com.trinity.banco.component;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class TestDataFactory {

    private TestDataFactory() {
    }

    public static String createClienteRequestJson(String tipoIdentificacion, String numeroIdentificacion,
                                                   String nombres, String apellidos,
                                                   String email, LocalDate fechaNacimiento) {
        return """
                {
                    "tipoIdentificacion": "%s",
                    "numeroIdentificacion": "%s",
                    "nombres": "%s",
                    "apellidos": "%s",
                    "email": "%s",
                    "fechaNacimiento": "%s"
                }
                """.formatted(tipoIdentificacion, numeroIdentificacion, nombres, apellidos,
                email, fechaNacimiento.toString());
    }

    public static String createValidClienteRequest() {
        return createClienteRequestJson(
                "CC",
                "1234567890",
                "Juan Carlos",
                "Perez Lopez",
                "juan.perez@email.com",
                LocalDate.of(1990, 5, 15)
        );
    }

    public static String createCuentaRequestJson(String tipoCuenta, BigDecimal saldoInicial,
                                                  boolean exentaGMF, Long clienteId) {
        return """
                {
                    "tipoCuenta": "%s",
                    "saldoInicial": %s,
                    "exentaGMF": %s,
                    "clienteId": %d
                }
                """.formatted(tipoCuenta, saldoInicial.toPlainString(),
                exentaGMF, clienteId);
    }

    public static String createSavingsAccountRequest(Long clienteId) {
        return createCuentaRequestJson("AHORROS", new BigDecimal("500000.00"), false, clienteId);
    }

    public static String createCheckingAccountRequest(Long clienteId) {
        return createCuentaRequestJson("CORRIENTE", new BigDecimal("500000.00"), false, clienteId);
    }

    public static String createMovimientoRequestJson(String numeroCuenta, BigDecimal monto) {
        return """
                {
                    "numeroCuenta": "%s",
                    "monto": %s
                }
                """.formatted(numeroCuenta, monto.toPlainString());
    }

    public static String createTransferenciaRequestJson(String cuentaOrigen, String cuentaDestino, BigDecimal monto) {
        return """
                {
                    "cuentaOrigenNumero": "%s",
                    "cuentaDestinoNumero": "%s",
                    "monto": %s
                }
                """.formatted(cuentaOrigen, cuentaDestino, monto.toPlainString());
    }
}
