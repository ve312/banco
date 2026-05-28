package com.trinity.banco.transaccion.application.usecases;

import com.trinity.banco.cuenta.application.validators.CuentaValidator;
import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.transaccion.application.util.GmfCalculator;
import com.trinity.banco.transaccion.domain.model.Transaccion;
import com.trinity.banco.transaccion.domain.model.enums.TipoTransaccion;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import com.trinity.banco.transaccion.domain.ports.TransaccionRepository;
import com.trinity.banco.shared.domain.errors.RecursoNoEncontradoException;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Transactional
public class RetirarUseCase {
    private final CuentaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;

    public RetirarUseCase(CuentaRepository cuentaRepository,
                          TransaccionRepository transaccionRepository) {
        this.cuentaRepository = cuentaRepository;
        this.transaccionRepository = transaccionRepository;
    }

    public Transaccion ejecutar(String numeroCuenta, BigDecimal monto) {

        CuentaValidator.validarMonto(monto);

        Cuenta cuenta = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada"));

        CuentaValidator.validarCuentaActiva(cuenta);

        BigDecimal gmf = GmfCalculator.calcularGmf(cuenta, monto);
        BigDecimal totalDebito = monto.add(gmf);

        CuentaValidator.validarSaldoDisponible(cuenta, totalDebito);

        BigDecimal saldoAnterior = cuenta.getSaldo();

        cuenta.setSaldo(saldoAnterior.subtract(totalDebito));
        cuenta.setFechaModificacion(LocalDateTime.now());

        BigDecimal saldoPosterior = cuenta.getSaldo();

        cuentaRepository.guardar(cuenta);

        Transaccion transaccion = new Transaccion(
                null,
                cuenta.getNumeroCuenta(),
                TipoTransaccion.RETIRO,
                monto,
                saldoAnterior,
                saldoPosterior,
                LocalDateTime.now(),
                null,
                gmf
        );

        transaccionRepository.guardar(transaccion);
        return transaccion;
    }
}
