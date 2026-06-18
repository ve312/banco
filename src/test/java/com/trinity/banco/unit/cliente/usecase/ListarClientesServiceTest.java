package com.trinity.banco.unit.cliente.usecase;

import com.trinity.banco.cliente.application.usecases.ListarClientesUseCase;
import com.trinity.banco.cliente.domain.model.Cliente;
import com.trinity.banco.cliente.domain.ports.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class ListarClientesServiceTest {
    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ListarClientesUseCase listarClientesService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deberia_listar_clientes_exitosamente() {
        List<Cliente> clientesMock = Arrays.asList(mock(Cliente.class), mock(Cliente.class));
        when(clienteRepository.listar()).thenReturn(clientesMock);

        List<Cliente> resultado = listarClientesService.ejecutar();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(clienteRepository, times(1)).listar();
    }
}
