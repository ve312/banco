package com.trinity.banco.Usecase.transaccion;

import com.trinity.banco.transaccion.application.usecases.ListarTransaccionesPorCuentaUseCase;
import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.transaccion.domain.model.Transaccion;
import com.trinity.banco.cuenta.domain.ports.CuentaRepository;
import com.trinity.banco.transaccion.domain.ports.TransaccionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


public class ListarTransaccionesPorCuentaServiceTest {
    @Mock
    private TransaccionRepository transaccionRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private ListarTransaccionesPorCuentaUseCase listarTransaccionesPorCuentaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_listar_transacciones_exitosamente() {
        when(cuentaRepository.buscarPorNumeroCuenta("53001")).thenReturn(Optional.of(mock(Cuenta.class)));
        when(transaccionRepository.listarPorNumeroCuenta("53001")).thenReturn(Arrays.asList(mock(Transaccion.class)));

        List<Transaccion> resultado = listarTransaccionesPorCuentaService.ejecutar("53001");

        assertFalse(resultado.isEmpty());
        verify(transaccionRepository).listarPorNumeroCuenta("53001");
    }

    @Test
    void deberia_lanzar_error_si_cuenta_no_existe() {
        when(cuentaRepository.buscarPorNumeroCuenta("53001")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                listarTransaccionesPorCuentaService.ejecutar("53001")
        );
    }
}
