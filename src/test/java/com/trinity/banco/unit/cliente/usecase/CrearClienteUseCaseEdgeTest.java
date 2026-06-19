package com.trinity.banco.unit.cliente.usecase;

import com.trinity.banco.cliente.application.usecases.CrearClienteUseCase;
import com.trinity.banco.cliente.domain.model.Cliente;
import com.trinity.banco.cliente.domain.model.enums.TipoIdentificacion;
import com.trinity.banco.cliente.domain.ports.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CrearClienteUseCaseEdgeTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private CrearClienteUseCase crearClienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_lanzar_error_si_edad_excede_120() {
        when(clienteRepository.existePorNumeroIdentificacion("123456789")).thenReturn(false);
        when(clienteRepository.existePorEmail("test@test.com")).thenReturn(false);

        LocalDate mayor120 = LocalDate.now().minusYears(121);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                crearClienteService.ejecutar(
                        TipoIdentificacion.CC, "123456789", "Maria", "Gomez",
                        "test@test.com", mayor120
                )
        );

        assertEquals("Edad no válida", ex.getMessage());
        verify(clienteRepository, never()).guardar(any());
    }

    @Test
    void deberia_lanzar_error_si_cliente_tiene_exactamente_18_anios() {
        when(clienteRepository.existePorNumeroIdentificacion("123456789")).thenReturn(false);
        when(clienteRepository.existePorEmail("test@test.com")).thenReturn(false);

        LocalDate exactamente18 = LocalDate.now().minusYears(18);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                crearClienteService.ejecutar(
                        TipoIdentificacion.CC, "123456789", "Maria", "Gomez",
                        "test@test.com", exactamente18
                )
        );

        assertEquals("El cliente debe ser mayor de edad", ex.getMessage());
    }

    @Test
    void deberia_aceptar_cliente_mayor_de_18() {
        when(clienteRepository.existePorNumeroIdentificacion("123456789")).thenReturn(false);
        when(clienteRepository.existePorEmail("test@test.com")).thenReturn(false);
        when(clienteRepository.guardar(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LocalDate mayor18 = LocalDate.now().minusYears(18).minusDays(1);

        assertDoesNotThrow(() ->
                crearClienteService.ejecutar(
                        TipoIdentificacion.CC, "123456789", "Maria", "Gomez",
                        "test@test.com", mayor18
                )
        );
    }
}
