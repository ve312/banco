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
import java.util.List;


@Transactional
public class TransferirUseCase {
    private final CuentaRepository cuentaRepository;
    private final TransaccionRepository transaccionRepository;

    public TransferirUseCase(CuentaRepository cuentaRepository,
                             TransaccionRepository transaccionRepository) {
        this.cuentaRepository = cuentaRepository;
        this.transaccionRepository = transaccionRepository;
    }

    public List<Transaccion> ejecutar(String cuentaOrigenNumero,
                                      String cuentaDestinoNumero,
                                      BigDecimal monto) {

        CuentaValidator.validarMonto(monto);

        if (cuentaOrigenNumero.equals(cuentaDestinoNumero)) {
            throw new RuntimeException("No se puede transferir a la misma cuenta");
        }

        Cuenta cuentaOrigen = cuentaRepository.buscarPorNumeroCuenta(cuentaOrigenNumero)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta origen no encontrada"));

        Cuenta cuentaDestino = cuentaRepository.buscarPorNumeroCuenta(cuentaDestinoNumero)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cuenta destino no encontrada"));

        CuentaValidator.validarCuentaActiva(cuentaOrigen);
        CuentaValidator.validarCuentaActiva(cuentaDestino);

        boolean mismoCliente = cuentaOrigen.getClienteId().equals(cuentaDestino.getClienteId());

        BigDecimal gmf = BigDecimal.ZERO;
        BigDecimal totalDebito = monto;

        if (!mismoCliente) {
            gmf = GmfCalculator.calcularGmf(cuentaOrigen, monto);
            totalDebito = monto.add(gmf);
        }

        CuentaValidator.validarSaldoDisponible(cuentaOrigen, totalDebito);

        BigDecimal saldoAnteriorOrigen = cuentaOrigen.getSaldo();
        BigDecimal saldoAnteriorDestino = cuentaDestino.getSaldo();

        cuentaOrigen.setSaldo(saldoAnteriorOrigen.subtract(totalDebito));
        cuentaOrigen.setFechaModificacion(LocalDateTime.now());

        cuentaDestino.setSaldo(saldoAnteriorDestino.add(monto));
        cuentaDestino.setFechaModificacion(LocalDateTime.now());

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
                cuentaDestino.getNumeroCuenta(),
                gmf
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

        return List.of(transaccionOrigen, transaccionDestino);
    }
}
