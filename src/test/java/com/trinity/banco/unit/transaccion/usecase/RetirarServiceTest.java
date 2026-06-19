package com.trinity.banco.unit.transaccion.usecase;

import com.trinity.banco.transaccion.application.usecases.RetirarUseCase;
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
import java.time.YearMonth;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RetirarServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private TransaccionRepository transaccionRepository;

    @InjectMocks
    private RetirarUseCase retirarService;

    private Cuenta cuenta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cuenta = new Cuenta(
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
    }

    @Test
    void deberia_retirar_con_gmf_si_cuenta_no_es_exenta() {
        when(cuentaRepository.buscarPorNumeroCuenta("5300000001"))
                .thenReturn(Optional.of(cuenta));

        Transaccion resultado = retirarService.ejecutar(
                "5300000001",
                new BigDecimal("200")
        );

        assertNotNull(resultado);
        assertEquals(new BigDecimal("200"), resultado.getMonto());
        assertEquals(new BigDecimal("0.80"), resultado.getImpuesto());

        verify(cuentaRepository).guardar(any());
        verify(transaccionRepository).guardar(any());
    }

    @Test
    void deberia_retirar_sin_gmf_si_cuenta_es_exenta_y_dentro_del_limite() {
        Cuenta cuentaExenta = new Cuenta(
                1L,
                TipoCuenta.AHORROS,
                "5300000001",
                EstadoCuenta.ACTIVA,
                new BigDecimal("20000000"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L,
                new BigDecimal("1000000"),
                YearMonth.now().getYear() * 100 + YearMonth.now().getMonthValue()
        );

        when(cuentaRepository.buscarPorNumeroCuenta("5300000001"))
                .thenReturn(Optional.of(cuentaExenta));

        Transaccion resultado = retirarService.ejecutar(
                "5300000001",
                new BigDecimal("50000")
        );

        assertNotNull(resultado);
        assertEquals(BigDecimal.ZERO, resultado.getImpuesto());
        verify(transaccionRepository).guardar(any());
    }

    @Test
    void deberia_retirar_con_gmf_solo_sobre_excedente_cuenta_exenta() {
        int mesActual = YearMonth.now().getYear() * 100 + YearMonth.now().getMonthValue();
        Cuenta cuentaExenta = new Cuenta(
                1L,
                TipoCuenta.AHORROS,
                "5300000001",
                EstadoCuenta.ACTIVA,
                new BigDecimal("20000000"),
                true,
                LocalDateTime.now(),
                LocalDateTime.now(),
                1L,
                new BigDecimal("17400000"),
                mesActual
        );

        when(cuentaRepository.buscarPorNumeroCuenta("5300000001"))
                .thenReturn(Optional.of(cuentaExenta));

        Transaccion resultado = retirarService.ejecutar(
                "5300000001",
                new BigDecimal("100000")
        );

        assertNotNull(resultado);
        assertEquals(BigDecimal.ZERO, resultado.getImpuesto());
        verify(transaccionRepository).guardar(any());
    }

    @Test
    void deberia_lanzar_error_si_cuenta_no_existe() {

        when(cuentaRepository.buscarPorNumeroCuenta("5300000001"))
                .thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                retirarService.ejecutar("5300000001", new BigDecimal("100"))
        );

        assertEquals("Cuenta no encontrada", ex.getMessage());
    }

    @Test
    void deberia_lanzar_error_si_monto_es_invalido() {

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                retirarService.ejecutar("5300000001", BigDecimal.ZERO)
        );

        assertEquals("El monto debe ser mayor a cero", ex.getMessage());
    }
}
