package com.trinity.banco.transaccion.application.usecases;

import com.trinity.banco.cuenta.application.validators.CuentaValidator;
import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.transaccion.domain.model.Transaccion;
import com.trinity.banco.transaccion.domain.model.enums.TipoTransaccion;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import com.trinity.banco.transaccion.domain.ports.TransaccionRepository;
import com.trinity.banco.shared.errors.RecursoNoEncontradoException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class RetirarService {
    private final CuentaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;

    public RetirarService(CuentaRepository cuentaRepository,
                          TransaccionRepository transaccionRepository) {
        this.cuentaRepository = cuentaRepository;
        this.transaccionRepository = transaccionRepository;
    }

    public Transaccion ejecutar(String numeroCuenta, BigDecimal monto) {

        CuentaValidator.validarMonto(monto);

        Cuenta cuenta = cuentaRepository.buscarPorNumeroCuenta(numeroCuenta)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta no encontrada"));

        CuentaValidator.validarCuentaActiva(cuenta);
        CuentaValidator.validarSaldoDisponible(cuenta, monto);

        BigDecimal saldoAnterior = cuenta.getSaldo();

        cuenta.setSaldo(saldoAnterior.subtract(monto));
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
                null
        );

        transaccionRepository.guardar(transaccion);
        return transaccion;
    }
}
