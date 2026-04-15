package com.trinity.banco.service.transaccion;

import com.trinity.banco.application.service.transaccion.RetirarService;
import com.trinity.banco.domain.model.Cuenta;
import com.trinity.banco.domain.model.Transaccion;
import com.trinity.banco.domain.model.enums.EstadoCuenta;
import com.trinity.banco.domain.model.enums.TipoCuenta;
import com.trinity.banco.domain.ports.repository.CuentaRepository;
import com.trinity.banco.domain.ports.repository.TransaccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private RetirarService retirarService;

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
    void deberia_retirar_exitosamente() {

        when(cuentaRepository.buscarPorNumeroCuenta("5300000001"))
                .thenReturn(Optional.of(cuenta));

        Transaccion resultado = retirarService.ejecutar(
                "5300000001",
                new BigDecimal("200")
        );

        assertNotNull(resultado);
        assertEquals(new BigDecimal("200"), resultado.getMonto());

        verify(cuentaRepository).guardar(any());
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
