package com.trinity.banco.unit.transaccion.usecase;

import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cuenta.domain.model.enums.EstadoCuenta;
import com.trinity.banco.cuenta.domain.model.enums.TipoCuenta;
import com.trinity.banco.transaccion.application.util.GmfCalculator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;

class GmfCalculatorTest {

    private static final BigDecimal TASA_GMF = new BigDecimal("0.004");

    private Cuenta crearCuentaNoExenta(BigDecimal saldo) {
        return new Cuenta(1L, TipoCuenta.AHORROS, "5300000001", EstadoCuenta.ACTIVA,
                saldo, false, LocalDateTime.now(), LocalDateTime.now(), 1L);
    }

    private Cuenta crearCuentaExenta(BigDecimal saldo, BigDecimal acumulado, int mes) {
        return new Cuenta(1L, TipoCuenta.AHORROS, "5300000001", EstadoCuenta.ACTIVA,
                saldo, true, LocalDateTime.now(), LocalDateTime.now(), 1L,
                acumulado, mes);
    }

    @Test
    void deberia_calcular_gmf_cuenta_no_exenta() {
        Cuenta cuenta = crearCuentaNoExenta(new BigDecimal("1000"));
        BigDecimal monto = new BigDecimal("200");
        BigDecimal esperado = monto.multiply(TASA_GMF).setScale(2, java.math.RoundingMode.HALF_UP);

        BigDecimal resultado = GmfCalculator.calcularGmf(cuenta, monto);

        assertEquals(esperado, resultado);
        assertEquals(new BigDecimal("0.80"), resultado);
    }

    @Test
    void deberia_retornar_cero_si_cuenta_exenta_no_excede_limite() {
        int mesActual = YearMonth.now().getYear() * 100 + YearMonth.now().getMonthValue();
        Cuenta cuenta = crearCuentaExenta(new BigDecimal("20000000"), new BigDecimal("10000000"), mesActual);

        BigDecimal resultado = GmfCalculator.calcularGmf(cuenta, new BigDecimal("100000"));

        assertEquals(BigDecimal.ZERO, resultado);
    }

    @Test
    void deberia_calcular_gmf_solo_sobre_excedente_cuenta_exenta() {
        int mesActual = YearMonth.now().getYear() * 100 + YearMonth.now().getMonthValue();
        BigDecimal limite = new BigDecimal("18330900");
        BigDecimal acumulado = new BigDecimal("18300000");
        Cuenta cuenta = crearCuentaExenta(new BigDecimal("20000000"), acumulado, mesActual);

        BigDecimal monto = new BigDecimal("50000");
        BigDecimal excedente = acumulado.add(monto).subtract(limite);
        BigDecimal esperado = excedente.multiply(TASA_GMF).setScale(2, java.math.RoundingMode.HALF_UP);

        BigDecimal resultado = GmfCalculator.calcularGmf(cuenta, monto);

        assertEquals(esperado, resultado);
        assertTrue(resultado.compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    void deberia_reiniciar_acumulado_si_cambio_de_mes() {
        int mesAnterior = 202601;
        Cuenta cuenta = crearCuentaExenta(new BigDecimal("20000000"), new BigDecimal("18330900"), mesAnterior);

        BigDecimal resultado = GmfCalculator.calcularGmf(cuenta, new BigDecimal("100000"));

        assertEquals(BigDecimal.ZERO, resultado);
        assertEquals(new BigDecimal("100000"), cuenta.getGmfAcumuladoMensual());
    }

    @Test
    void deberia_calcular_gmf_para_monto_grande() {
        Cuenta cuenta = crearCuentaNoExenta(new BigDecimal("100000000"));
        BigDecimal monto = new BigDecimal("10000000");

        BigDecimal resultado = GmfCalculator.calcularGmf(cuenta, monto);

        assertEquals(new BigDecimal("40000.00"), resultado);
    }

    @Test
    void deberia_calcular_gmf_cuando_excede_justo_el_limite() {
        int mesActual = YearMonth.now().getYear() * 100 + YearMonth.now().getMonthValue();
        BigDecimal limite = new BigDecimal("18330900");
        Cuenta cuenta = crearCuentaExenta(new BigDecimal("20000000"), limite, mesActual);

        BigDecimal monto = new BigDecimal("100000");
        BigDecimal excedente = limite.add(monto).subtract(limite);
        BigDecimal esperado = excedente.multiply(TASA_GMF).setScale(2, java.math.RoundingMode.HALF_UP);

        BigDecimal resultado = GmfCalculator.calcularGmf(cuenta, monto);

        assertEquals(esperado, resultado);
        assertEquals(new BigDecimal("400.00"), resultado);
    }
}
