package com.trinity.banco.transaccion.application.util;

import com.trinity.banco.cuenta.domain.model.Cuenta;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;

public class GmfCalculator {

    private static final BigDecimal TASA_GMF = new BigDecimal("0.004");

    private GmfCalculator() {}

    public static BigDecimal calcularGmf(Cuenta cuenta, BigDecimal montoTransaccion) {
        if (cuenta.isExentaGMF()) {
            return calcularGmfExenta(cuenta, montoTransaccion);
        }
        return montoTransaccion.multiply(TASA_GMF).setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal calcularGmfExenta(Cuenta cuenta, BigDecimal montoTransaccion) {
        int mesActual = obtenerMesActual();

        if (!cuenta.getMesAcumuladoGMF().equals(mesActual)) {
            cuenta.setGmfAcumuladoMensual(BigDecimal.ZERO);
            cuenta.setMesAcumuladoGMF(mesActual);
        }

        BigDecimal limite = cuenta.getLimiteGmfMensual();
        BigDecimal acumulado = cuenta.getGmfAcumuladoMensual();

        BigDecimal nuevoAcumulado = acumulado.add(montoTransaccion);

        if (nuevoAcumulado.compareTo(limite) <= 0) {
            cuenta.setGmfAcumuladoMensual(nuevoAcumulado);
            return BigDecimal.ZERO;
        }

        BigDecimal excedente = nuevoAcumulado.subtract(limite);
        cuenta.setGmfAcumuladoMensual(limite);

        return excedente.multiply(TASA_GMF).setScale(2, RoundingMode.HALF_UP);
    }

    static int obtenerMesActual() {
        YearMonth ym = YearMonth.now();
        return ym.getYear() * 100 + ym.getMonthValue();
    }
}
