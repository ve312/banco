package com.trinity.banco.service.cuenta;

import com.trinity.banco.application.service.cuenta.InactivarCuentaService;
import com.trinity.banco.domain.model.Cuenta;
import com.trinity.banco.domain.ports.repository.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class InactivarCuentaServiceTest {
    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private InactivarCuentaService inactivarCuentaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_inactivar_cuenta_exitosamente() {
        Cuenta cuentaMock = mock(Cuenta.class);
        when(cuentaRepository.buscarPorNumeroCuenta("5300000001")).thenReturn(Optional.of(cuentaMock));

        inactivarCuentaService.ejecutar("5300000001");

        verify(cuentaMock, times(1)).inactivar();
        verify(cuentaRepository, times(1)).guardar(cuentaMock);
    }
}
