package com.trinity.banco.unit.transaccion.usecase;

import com.trinity.banco.transaccion.application.usecases.TransferirUseCase;
import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.transaccion.domain.model.Transaccion;
import com.trinity.banco.cuenta.domain.model.enums.EstadoCuenta;
import com.trinity.banco.cuenta.domain.model.enums.TipoCuenta;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import com.trinity.banco.transaccion.domain.ports.TransaccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransferirServiceTest {
    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private TransaccionRepository transaccionRepository;

    @InjectMocks
    private TransferirUseCase transferirService;

    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cuentaOrigen = new Cuenta(
                1L,
                TipoCuenta.AHORROS,
                "5300000001",
                EstadoCuenta.ACTIVA,
                new BigDecimal("1000"),
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L
        );

        cuentaDestino = new Cuenta(
                2L,
                TipoCuenta.AHORROS,
                "5300000002",
                EstadoCuenta.ACTIVA,
                new BigDecimal("500"),
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                2L
        );
    }


    @Test
    void deberia_transferir_con_gmf_entre_distintos_clientes() {

        when(cuentaRepository.buscarPorNumeroCuenta("5300000001"))
                .thenReturn(Optional.of(cuentaOrigen));

        when(cuentaRepository.buscarPorNumeroCuenta("5300000002"))
                .thenReturn(Optional.of(cuentaDestino));

        List<Transaccion> resultado = transferirService.ejecutar(
                "5300000001",
                "5300000002",
                new BigDecimal("200")
        );

        assertNotNull(resultado);
        assertEquals(2, resultado.size());

        Transaccion origenTx = resultado.get(0);
        assertEquals(new BigDecimal("200"), origenTx.getMonto());
        assertEquals(new BigDecimal("0.80"), origenTx.getImpuesto());

        Transaccion destTx = resultado.get(1);
        assertEquals(BigDecimal.ZERO, destTx.getImpuesto());

        verify(cuentaRepository, times(2)).guardar(any());
        verify(transaccionRepository, times(2)).guardar(any());
    }


    @Test
    void deberia_transferir_sin_gmf_entre_cuentas_del_mismo_cliente() {
        Cuenta cuentaOrigenMismoCliente = new Cuenta(
                1L,
                TipoCuenta.AHORROS,
                "5300000001",
                EstadoCuenta.ACTIVA,
                new BigDecimal("1000"),
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L
        );

        Cuenta cuentaDestinoMismoCliente = new Cuenta(
                2L,
                TipoCuenta.CORRIENTE,
                "5300000002",
                EstadoCuenta.ACTIVA,
                new BigDecimal("500"),
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L
        );

        when(cuentaRepository.buscarPorNumeroCuenta("5300000001"))
                .thenReturn(Optional.of(cuentaOrigenMismoCliente));

        when(cuentaRepository.buscarPorNumeroCuenta("5300000002"))
                .thenReturn(Optional.of(cuentaDestinoMismoCliente));

        List<Transaccion> resultado = transferirService.ejecutar(
                "5300000001",
                "5300000002",
                new BigDecimal("200")
        );

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals(BigDecimal.ZERO, resultado.get(0).getImpuesto());

        verify(cuentaRepository, times(2)).guardar(any());
        verify(transaccionRepository, times(2)).guardar(any());
    }


    @Test
    void deberia_lanzar_error_si_cuenta_origen_no_existe() {

        when(cuentaRepository.buscarPorNumeroCuenta("5300000001"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                transferirService.ejecutar("5300000001", "5300000002", new BigDecimal("100"))
        );

        assertEquals("Cuenta origen no encontrada", ex.getMessage());
    }


    @Test
    void deberia_lanzar_error_si_es_la_misma_cuenta() {

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                transferirService.ejecutar("5300000001", "5300000001", new BigDecimal("100"))
        );

        assertEquals("No se puede transferir a la misma cuenta", ex.getMessage());
    }

    @Test
    void deberia_lanzar_error_si_saldo_insuficiente_con_gmf() {
        Cuenta cuentaOrigenPobre = new Cuenta(
                1L,
                TipoCuenta.AHORROS,
                "5300000001",
                EstadoCuenta.ACTIVA,
                new BigDecimal("100"),
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L
        );

        when(cuentaRepository.buscarPorNumeroCuenta("5300000001"))
                .thenReturn(Optional.of(cuentaOrigenPobre));

        when(cuentaRepository.buscarPorNumeroCuenta("5300000002"))
                .thenReturn(Optional.of(cuentaDestino));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                transferirService.ejecutar("5300000001", "5300000002", new BigDecimal("100"))
        );

        assertEquals("Cuenta de ahorros no puede quedar en saldo negativo", ex.getMessage());
        verify(transaccionRepository, never()).guardar(any());
    }
}
