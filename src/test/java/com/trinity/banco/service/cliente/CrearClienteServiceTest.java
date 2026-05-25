package com.trinity.banco.service.cliente;

import com.trinity.banco.cliente.application.usecases.CrearClienteService;
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

public class CrearClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private CrearClienteService crearClienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_crear_cliente_exitosamente() {
        when(clienteRepository.existePorNumeroIdentificacion("123456789")).thenReturn(false);
        when(clienteRepository.guardar(any(Cliente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LocalDate fechaNacimientoValida = LocalDate.now().minusYears(20);

        Cliente resultado = crearClienteService.ejecutar(
                TipoIdentificacion.CC,
                "123456789",
                "Maria",
                "Gomez",
                "maria@test.com",
                fechaNacimientoValida
        );

        assertNotNull(resultado);
        assertEquals("123456789", resultado.getNumeroIdentificacion());
        verify(clienteRepository, times(1)).guardar(any(Cliente.class));
    }

    @Test
    void deberia_lanzar_error_si_cliente_ya_existe() {
        when(clienteRepository.existePorNumeroIdentificacion("123456789")).thenReturn(true);

        LocalDate fechaNacimientoValida = LocalDate.now().minusYears(20);

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                crearClienteService.ejecutar(
                        TipoIdentificacion.CC,
                        "123456789",
                        "Maria",
                        "Gomez",
                        "maria@test.com",
                        fechaNacimientoValida
                )
        );

        assertEquals("El cliente ya existe", ex.getMessage());
        verify(clienteRepository, never()).guardar(any());
    }

    @Test
    void deberia_lanzar_error_si_es_menor_de_edad() {
        when(clienteRepository.existePorNumeroIdentificacion("123456789")).thenReturn(false);

        LocalDate fechaNacimientoInvalida = LocalDate.now().minusYears(15);
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                crearClienteService.ejecutar(
                        TipoIdentificacion.CC,
                        "123456789",
                        "Maria",
                        "Gomez",
                        "maria@test.com",
                        fechaNacimientoInvalida
                )
        );

        assertEquals("El cliente debe ser mayor de edad", ex.getMessage());
        verify(clienteRepository, never()).guardar(any());
    }
}
