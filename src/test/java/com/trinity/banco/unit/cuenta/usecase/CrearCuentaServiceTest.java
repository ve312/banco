package com.trinity.banco.unit.cuenta.usecase;

import com.trinity.banco.cuenta.application.usecases.CrearCuentaUseCase;
import com.trinity.banco.cuenta.application.util.NumeroCuentaGenerator;
import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cuenta.domain.model.enums.TipoCuenta;
import com.trinity.banco.cliente.domain.ports.ClienteRepository;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CrearCuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private NumeroCuentaGenerator numeroCuentaGenerator;

    @InjectMocks
    private CrearCuentaUseCase crearCuentaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_crear_cuenta_exitosamente() {
        when(clienteRepository.existePorId(1L)).thenReturn(true);
        when(cuentaRepository.listarPorClienteId(1L)).thenReturn(List.of());
        when(numeroCuentaGenerator.generar(TipoCuenta.AHORROS)).thenReturn("5300000001");
        when(cuentaRepository.guardar(any(Cuenta.class))).thenAnswer(i -> i.getArgument(0));

        Cuenta resultado = crearCuentaService.ejecutar(
                TipoCuenta.AHORROS,
                new BigDecimal("1000"),
                true,
                1L
        );

        assertNotNull(resultado);
        assertEquals("5300000001", resultado.getNumeroCuenta());
        verify(cuentaRepository, times(1)).guardar(any(Cuenta.class));
    }

    @Test
    void deberia_crear_cuenta_no_exenta_sin_validacion_duplicado() {
        when(clienteRepository.existePorId(1L)).thenReturn(true);
        when(numeroCuentaGenerator.generar(TipoCuenta.AHORROS)).thenReturn("5300000001");
        when(cuentaRepository.guardar(any(Cuenta.class))).thenAnswer(i -> i.getArgument(0));

        Cuenta resultado = crearCuentaService.ejecutar(
                TipoCuenta.AHORROS,
                new BigDecimal("500"),
                false,
                1L
        );

        assertNotNull(resultado);
        assertFalse(resultado.isExentaGMF());
        verify(cuentaRepository, times(1)).guardar(any(Cuenta.class));
    }

    @Test
    void deberia_lanzar_error_si_segunda_cuenta_exenta_para_mismo_cliente() {
        Cuenta cuentaExentaExistente = mock(Cuenta.class);
        when(cuentaExentaExistente.isExentaGMF()).thenReturn(true);

        when(clienteRepository.existePorId(1L)).thenReturn(true);
        when(cuentaRepository.listarPorClienteId(1L)).thenReturn(List.of(cuentaExentaExistente));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                crearCuentaService.ejecutar(TipoCuenta.AHORROS, new BigDecimal("1000"), true, 1L)
        );

        assertEquals("El cliente ya tiene una cuenta exenta de GMF", ex.getMessage());
        verify(cuentaRepository, never()).guardar(any());
    }

    @Test
    void deberia_lanzar_error_si_cliente_no_existe() {
        when(clienteRepository.existePorId(1L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                crearCuentaService.ejecutar(TipoCuenta.AHORROS, new BigDecimal("1000"), true, 1L)
        );

        assertEquals("El cliente no existe", ex.getMessage());
        verify(cuentaRepository, never()).guardar(any());
    }
}
