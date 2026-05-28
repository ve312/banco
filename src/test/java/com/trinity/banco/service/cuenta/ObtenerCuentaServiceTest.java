package com.trinity.banco.service.cuenta;

import com.trinity.banco.cuenta.application.usecases.ObtenerCuentaUseCase;
import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ObtenerCuentaServiceTest {
    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private ObtenerCuentaUseCase obtenerCuentaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_obtener_cuenta_exitosamente() {
        Cuenta cuentaMock = mock(Cuenta.class);
        when(cuentaRepository.buscarPorNumeroCuenta("5300000001")).thenReturn(Optional.of(cuentaMock));

        Cuenta resultado = obtenerCuentaService.ejecutar("5300000001");

        assertNotNull(resultado);
        verify(cuentaRepository, times(1)).buscarPorNumeroCuenta("5300000001");
    }
}
