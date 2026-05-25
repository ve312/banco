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
import java.util.List;


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
        CuentaValidator.validarSaldoDisponible(cuentaOrigen, monto);

        BigDecimal saldoAnteriorOrigen = cuentaOrigen.getSaldo();
        BigDecimal saldoAnteriorDestino = cuentaDestino.getSaldo();

        cuentaOrigen.setSaldo(saldoAnteriorOrigen.subtract(monto));
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

        return List.of(transaccionOrigen, transaccionDestino);
    }
}
