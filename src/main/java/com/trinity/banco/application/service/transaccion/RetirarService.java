package com.trinity.banco.application.service.transaccion;

import com.trinity.banco.application.validator.CuentaValidator;
import com.trinity.banco.domain.model.Cuenta;
import com.trinity.banco.domain.model.Transaccion;
import com.trinity.banco.domain.model.enums.TipoTransaccion;
import com.trinity.banco.domain.ports.repository.CuentaRepository;
import com.trinity.banco.domain.ports.repository.TransaccionRepository;
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
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        BigDecimal saldoAnterior = cuenta.getSaldo();

        cuenta.retirar(monto);

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
