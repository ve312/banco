package com.trinity.banco.controller;

import com.trinity.banco.cuenta.application.usecases.*;
import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cuenta.domain.model.enums.EstadoCuenta;
import com.trinity.banco.cuenta.domain.model.enums.TipoCuenta;
import com.trinity.banco.cuenta.infrastructure.inbound.CuentaController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
public class CuentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CrearCuentaService crearCuentaService;
    @MockitoBean
    private CancelarCuentaService cancelarCuentaService;
    @MockitoBean
    private ActivarCuentaService activarCuentaService;
    @MockitoBean
    private InactivarCuentaService inactivarCuentaService;
    @MockitoBean
    private ObtenerCuentaService obtenerCuentaService;
    @MockitoBean
    private ListarCuentasPorClienteService listarPorClienteService;

    private Cuenta mockCuenta(String numeroCuenta) {
        Cuenta cuenta = mock(Cuenta.class);
        when(cuenta.getId()).thenReturn(1L);
        when(cuenta.getTipoCuenta()).thenReturn(TipoCuenta.AHORROS);
        when(cuenta.getNumeroCuenta()).thenReturn(numeroCuenta);
        when(cuenta.getEstado()).thenReturn(EstadoCuenta.ACTIVA);
        when(cuenta.getSaldo()).thenReturn(new BigDecimal("1000.0"));
        when(cuenta.isExentaGMF()).thenReturn(true);
        when(cuenta.getFechaCreacion()).thenReturn(LocalDateTime.now());
        when(cuenta.getFechaModificacion()).thenReturn(LocalDateTime.now());
        when(cuenta.getClienteId()).thenReturn(100L);
        return cuenta;
    }

    @Test
    void deberiaCrearCuenta() throws Exception {
        Cuenta cuentaMock = mockCuenta("123456");
        when(crearCuentaService.ejecutar(any(), any(), anyBoolean(), any())).thenReturn(cuentaMock);

        String json = """
            {
              "tipoCuenta": "AHORROS",
              "saldoInicial": 1000.0,
              "exentaGMF": true,
              "clienteId": 100
            }
            """;

        mockMvc.perform(post("/cuentas")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCuenta").value("123456"))
                .andExpect(jsonPath("$.clienteId").value(100));
    }

    @Test
    void deberiaObtenerCuentaPorNumero() throws Exception {
        Cuenta cuentaMock = mockCuenta("123456");
        when(obtenerCuentaService.ejecutar("123456")).thenReturn(cuentaMock);

        mockMvc.perform(get("/cuentas/123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCuenta").value("123456"));
    }

    @Test
    void deberiaListarCuentasPorCliente() throws Exception {
        Cuenta cuentaMock = mockCuenta("123456");
        when(listarPorClienteService.ejecutar(100L)).thenReturn(List.of(cuentaMock));

        mockMvc.perform(get("/cuentas/cliente/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroCuenta").value("123456"));
    }

    @Test
    void deberiaCancelarCuenta() throws Exception {
        doNothing().when(cancelarCuentaService).ejecutar("123456");

        mockMvc.perform(patch("/cuentas/123456/cancelar"))
                .andExpect(status().isNoContent());

        verify(cancelarCuentaService).ejecutar("123456");
    }

    @Test
    void deberiaActivarCuenta() throws Exception {
        doNothing().when(activarCuentaService).ejecutar("123456");

        mockMvc.perform(patch("/cuentas/123456/activar"))
                .andExpect(status().isNoContent());

        verify(activarCuentaService).ejecutar("123456");
    }

    @Test
    void deberiaInactivarCuenta() throws Exception {
        doNothing().when(inactivarCuentaService).ejecutar("123456");

        mockMvc.perform(patch("/cuentas/123456/inactivar"))
                .andExpect(status().isNoContent());

        verify(inactivarCuentaService).ejecutar("123456");
    }
}
