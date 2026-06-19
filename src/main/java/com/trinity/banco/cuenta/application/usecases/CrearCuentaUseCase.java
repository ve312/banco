package com.trinity.banco.cuenta.application.usecases;

import com.trinity.banco.cuenta.application.validators.CuentaValidator;
import com.trinity.banco.cuenta.application.util.NumeroCuentaGenerator;
import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cuenta.domain.model.enums.EstadoCuenta;
import com.trinity.banco.cuenta.domain.model.enums.TipoCuenta;
import com.trinity.banco.cliente.domain.ports.ClienteRepository;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CrearCuentaUseCase {
    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final NumeroCuentaGenerator numeroCuentaGenerator;

    public CrearCuentaUseCase(CuentaRepository cuentaRepository,
                              ClienteRepository clienteRepository,
                              NumeroCuentaGenerator numeroCuentaGenerator) {
        this.cuentaRepository = cuentaRepository;
        this.clienteRepository = clienteRepository;
        this.numeroCuentaGenerator = numeroCuentaGenerator;
    }

    public Cuenta ejecutar(TipoCuenta tipoCuenta,
                           BigDecimal saldoInicial,
                           boolean exentaGMF,
                           Long clienteId) {

        if (!clienteRepository.existePorId(clienteId)) {
            throw new RuntimeException("El cliente no existe");
        }

        if (exentaGMF) {
            boolean tieneCuentaExenta = cuentaRepository.listarPorClienteId(clienteId)
                    .stream()
                    .anyMatch(Cuenta::isExentaGMF);
            if (tieneCuentaExenta) {
                throw new RuntimeException("El cliente ya tiene una cuenta exenta de GMF");
            }
        }

        CuentaValidator.validarTipoCuenta(tipoCuenta);
        CuentaValidator.validarSaldoInicial(tipoCuenta,saldoInicial);

        String numeroCuenta = numeroCuentaGenerator.generar(tipoCuenta);

        LocalDateTime ahora = LocalDateTime.now();

        Cuenta cuenta = new Cuenta(
                null,
                tipoCuenta,
                numeroCuenta,
                EstadoCuenta.ACTIVA,
                saldoInicial,
                exentaGMF,
                ahora,
                ahora,
                clienteId
        );

        return cuentaRepository.guardar(cuenta);
    }

}
