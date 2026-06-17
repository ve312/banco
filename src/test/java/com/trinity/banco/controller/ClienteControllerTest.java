package com.trinity.banco.controller;

import com.trinity.banco.cliente.application.usecases.*;
import com.trinity.banco.cliente.domain.model.Cliente;
import com.trinity.banco.cliente.domain.model.enums.TipoIdentificacion;
import com.trinity.banco.cliente.infrastructure.inbound.ClienteController;
import com.trinity.banco.shared.infrastructure.security.CustomUserDetailsService;
import com.trinity.banco.shared.infrastructure.security.JwtProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtProvider jwtProvider;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @MockitoBean
    private CrearClienteUseCase crearClienteService;

    @MockitoBean
    private ActualizarClienteUseCase actualizarClienteService;

    @MockitoBean
    private EliminarClienteUseCase eliminarClienteService;

    @MockitoBean
    private ObtenerClienteUseCase obtenerClienteService;

    @MockitoBean
    private ListarClientesUseCase listarClientesService;

    private Cliente mockCliente() {
        Cliente cliente = mock(Cliente.class);
        when(cliente.getId()).thenReturn(1L);
        when(cliente.getTipoIdentificacion()).thenReturn(TipoIdentificacion.CC);
        when(cliente.getNumeroIdentificacion()).thenReturn("12345678");
        when(cliente.getNombres()).thenReturn("Juan");
        when(cliente.getApellidos()).thenReturn("Pérez");
        when(cliente.getEmail()).thenReturn("juan@test.com");
        when(cliente.getFechaNacimiento()).thenReturn(LocalDate.of(1990, 1, 1));
        return cliente;
    }

    @Test
    void deberiaCrearCliente() throws Exception {
        Cliente clienteMock = mockCliente();
        when(crearClienteService.ejecutar(any(), any(), any(), any(), any(), any())).thenReturn(clienteMock);

        String json = """
            {
              "tipoIdentificacion": "CC",
              "numeroIdentificacion": "12345678",
              "nombres": "Juan",
              "apellidos": "Pérez",
              "email": "juan@test.com",
              "fechaNacimiento": "1990-01-01"
            }
            """;

        mockMvc.perform(post("/clientes")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombres").value("Juan"))
                .andExpect(jsonPath("$.email").value("juan@test.com"));
    }

    @Test
    void deberiaActualizarCliente() throws Exception {
        Cliente clienteMock = mockCliente();
        when(actualizarClienteService.ejecutar(eq(1L), any(), any(), any())).thenReturn(clienteMock);

        String json = """
            {
              "nombres": "Juan",
              "apellidos": "Pérez",
              "email": "juan@test.com"
            }
            """;

        mockMvc.perform(put("/clientes/1")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombres").value("Juan"));
    }

    @Test
    void deberiaObtenerCliente() throws Exception {
        Cliente clienteMock = mockCliente();
        when(obtenerClienteService.ejecutar(1L)).thenReturn(clienteMock);

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombres").value("Juan"));
    }

    @Test
    void deberiaListarClientes() throws Exception {
        Cliente clienteMock = mockCliente();
        when(listarClientesService.ejecutar()).thenReturn(List.of(clienteMock));

        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombres").value("Juan"));
    }

    @Test
    void deberiaEliminarCliente() throws Exception {
        doNothing().when(eliminarClienteService).ejecutar(1L);

        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isNoContent());

        verify(eliminarClienteService).ejecutar(1L);
    }
}
