package com.trinity.banco.controller;

import com.trinity.banco.application.service.transaccion.ConsignarService;
import com.trinity.banco.application.service.transaccion.ListarTransaccionesPorCuentaService;
import com.trinity.banco.application.service.transaccion.RetirarService;
import com.trinity.banco.application.service.transaccion.TransferirService;
import com.trinity.banco.domain.model.Transaccion;
import com.trinity.banco.domain.model.enums.TipoTransaccion;
import com.trinity.banco.rest.controller.TransaccionController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransaccionController.class)
public class TransaccionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConsignarService consignarService;

    @MockitoBean
    private RetirarService retirarService;

    @MockitoBean
    private TransferirService transferirService;

    @MockitoBean
    private ListarTransaccionesPorCuentaService listarService;

    private Transaccion mockTransaccion(String numeroCuenta, BigDecimal monto,TipoTransaccion tipoTransaccion) {
        Transaccion transaccion = mock(Transaccion.class);
        when(transaccion.getId()).thenReturn(1L);
        when(transaccion.getNumeroCuenta()).thenReturn(numeroCuenta);
        when(transaccion.getTipoTransaccion()).thenReturn(tipoTransaccion);
        when(transaccion.getMonto()).thenReturn(monto);
        when(transaccion.getSaldoAnterior()).thenReturn(new BigDecimal("1000.0"));
        when(transaccion.getSaldoPosterior()).thenReturn(new BigDecimal("1500.0"));
        when(transaccion.getFecha()).thenReturn(LocalDateTime.now());
        when(transaccion.getNumeroCuentaRelacionada()).thenReturn("123456789");
        return transaccion;
    }

    @Test
    void deberiaConsignar() throws Exception {
        Transaccion transaccionMock = mockTransaccion("12345", new BigDecimal("500.00"), TipoTransaccion.CONSIGNACION);
        when(consignarService.ejecutar(eq("12345"), any())).thenReturn(transaccionMock);

        String json = """
            {
              "numeroCuenta": "12345",
              "monto": 500.00
            }
            """;

        mockMvc.perform(post("/transacciones/consignar")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCuenta").value("12345"))
                .andExpect(jsonPath("$.monto").value(500.0));
    }

    @Test
    void deberiaRetirar() throws Exception {
        Transaccion transaccionMock = mockTransaccion("12345", new BigDecimal("200.00"), TipoTransaccion.RETIRO);
        when(retirarService.ejecutar(eq("12345"), any())).thenReturn(transaccionMock);

        String json = """
            {
              "numeroCuenta": "12345",
              "monto": 200.00
            }
            """;

        mockMvc.perform(post("/transacciones/retirar")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroCuenta").value("12345"))
                .andExpect(jsonPath("$.monto").value(200.0));
    }

    @Test
    void deberiaTransferir() throws Exception {
        Transaccion t1 = mockTransaccion("123", new BigDecimal("300.00"),TipoTransaccion.CONSIGNACION);
        Transaccion t2 = mockTransaccion("456", new BigDecimal("300.00"),TipoTransaccion.RETIRO);

        when(transferirService.ejecutar(eq("123"), eq("456"), any())).thenReturn(List.of(t1, t2));

        String json = """
            {
              "cuentaOrigenNumero": "123",
              "cuentaDestinoNumero": "456",
              "monto": 300.00
            }
            """;

        mockMvc.perform(post("/transacciones/transferir")
                        .contentType("application/json")
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].numeroCuenta").value("123"))
                .andExpect(jsonPath("$[1].numeroCuenta").value("456"));
    }

    @Test
    void deberiaListarTransaccionesPorCuenta() throws Exception {
        Transaccion transaccionMock = mockTransaccion("12345", new BigDecimal("100.00"), TipoTransaccion.CONSIGNACION);
        when(listarService.ejecutar("12345")).thenReturn(List.of(transaccionMock));

        mockMvc.perform(get("/transacciones/cuenta/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroCuenta").value("12345"));
    }
}
