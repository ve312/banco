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
public class TransferirService {
    private final CuentaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;

    public TransferirService(CuentaRepository cuentaRepository,
                             TransaccionRepository transaccionRepository) {
        this.cuentaRepository = cuentaRepository;
        this.transaccionRepository = transaccionRepository;
    }

    public void ejecutar(String cuentaOrigenNumero,
                         String cuentaDestinoNumero,
                         BigDecimal monto) {

        CuentaValidator.validarMonto(monto);

        if (cuentaOrigenNumero.equals(cuentaDestinoNumero)) {
            throw new RuntimeException("No se puede transferir a la misma cuenta");
        }

        Cuenta cuentaOrigen = cuentaRepository.buscarPorNumeroCuenta(cuentaOrigenNumero)
                .orElseThrow(() -> new RuntimeException("Cuenta origen no encontrada"));

        Cuenta cuentaDestino = cuentaRepository.buscarPorNumeroCuenta(cuentaDestinoNumero)
                .orElseThrow(() -> new RuntimeException("Cuenta destino no encontrada"));


        BigDecimal saldoAnteriorOrigen = cuentaOrigen.getSaldo();
        BigDecimal saldoAnteriorDestino = cuentaDestino.getSaldo();

        cuentaOrigen.retirar(monto);
        cuentaDestino.depositar(monto);

        BigDecimal saldoPosteriorOrigen = cuentaOrigen.getSaldo();
        BigDecimal saldoPosteriorDestino = cuentaDestino.getSaldo();

        cuentaRepository.guardar(cuentaOrigen);
        cuentaRepository.guardar(cuentaDestino);

        Transaccion transaccionOrigen = new Transaccion(
                null,
                cuentaOrigen.getNumeroCuenta(),
                TipoTransaccion.TRANSFERENCIA,
                monto,
                saldoAnteriorOrigen,
                saldoPosteriorOrigen,
                LocalDateTime.now(),
                cuentaDestino.getNumeroCuenta()
        );

        Transaccion transaccionDestino = new Transaccion(
                null,
                cuentaDestino.getNumeroCuenta(),
                TipoTransaccion.TRANSFERENCIA,
                monto,
                saldoAnteriorDestino,
                saldoPosteriorDestino,
                LocalDateTime.now(),
                cuentaOrigen.getNumeroCuenta()
        );

        transaccionRepository.guardar(transaccionOrigen);
        transaccionRepository.guardar(transaccionDestino);
    }
}
