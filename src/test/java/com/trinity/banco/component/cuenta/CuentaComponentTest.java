package com.trinity.banco.component.cuenta;

import com.trinity.banco.component.AbstractBaseIntegrationTest;
import com.trinity.banco.component.TestDataFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CuentaComponentTest extends AbstractBaseIntegrationTest {

    private static final String CLIENTES_PATH = "/clientes";
    private static final String CUENTAS_PATH = "/cuentas";
    private static int clientCounter = 0;

    private Long createTestClient(String token, String uniqueSuffix) throws Exception {
        clientCounter++;
        String apellido = "Prueba" + uniqueSuffix.replaceAll("[^A-Za-z]", "");
        String numeroIdent = String.format("300000%04d", clientCounter);
        String clienteJson = TestDataFactory.createClienteRequestJson(
                "CC",
                numeroIdent,
                "Cliente",
                apellido.isEmpty() ? "Prueba" : apellido,
                "cliente." + uniqueSuffix + "@email.com",
                LocalDate.of(1985, 3, 10)
        );

        String response = mockMvc.perform(post(CLIENTES_PATH)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(clienteJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return Long.parseLong(
                response.substring(response.indexOf("\"id\":") + 5,
                        response.indexOf(",", response.indexOf("\"id\":") + 5))
        );
    }

    @Test
    void shouldCreateSavingsAccountWithCorrectPrefix() throws Exception {
        String token = getAdminToken();
        Long clientId = createTestClient(token, "sav001");

        mockMvc.perform(post(CUENTAS_PATH)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(TestDataFactory.createSavingsAccountRequest(clientId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoCuenta").value("AHORROS"))
                .andExpect(jsonPath("$.numeroCuenta").isString())
                .andExpect(jsonPath("$.numeroCuenta").value(org.hamcrest.Matchers.startsWith("53")))
                .andExpect(jsonPath("$.numeroCuenta").value(org.hamcrest.Matchers.hasLength(10)))
                .andExpect(jsonPath("$.estado").value("ACTIVA"))
                .andExpect(jsonPath("$.saldo").value(500000.00))
                .andExpect(jsonPath("$.exentaGMF").value(false))
                .andExpect(jsonPath("$.clienteId").value(clientId));
    }

    @Test
    void shouldCreateCheckingAccountWithCorrectPrefix() throws Exception {
        String token = getAdminToken();
        Long clientId = createTestClient(token, "chk002");

        mockMvc.perform(post(CUENTAS_PATH)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(TestDataFactory.createCheckingAccountRequest(clientId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipoCuenta").value("CORRIENTE"))
                .andExpect(jsonPath("$.numeroCuenta").isString())
                .andExpect(jsonPath("$.numeroCuenta").value(org.hamcrest.Matchers.startsWith("33")))
                .andExpect(jsonPath("$.numeroCuenta").value(org.hamcrest.Matchers.hasLength(10)))
                .andExpect(jsonPath("$.estado").value("ACTIVA"))
                .andExpect(jsonPath("$.exentaGMF").value(false))
                .andExpect(jsonPath("$.clienteId").value(clientId));
    }

    @Test
    void shouldReturnBadRequestWhenCancellingAccountWithBalance() throws Exception {
        String token = getAdminToken();
        Long clientId = createTestClient(token, "cancel003");

        String accountResponse = mockMvc.perform(post(CUENTAS_PATH)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(TestDataFactory.createSavingsAccountRequest(clientId)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String numeroCuenta = accountResponse.substring(
                accountResponse.indexOf("\"numeroCuenta\":\"") + 16,
                accountResponse.indexOf("\"", accountResponse.indexOf("\"numeroCuenta\":\"") + 16)
        );

        mockMvc.perform(patch(CUENTAS_PATH + "/" + numeroCuenta + "/cancelar")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("No se puede cancelar una cuenta con saldo diferente a 0"));
    }

    @Test
    void shouldCancelAccountWhenBalanceIsZero() throws Exception {
        String token = getAdminToken();
        Long clientId = createTestClient(token, "cancelok004");

        String accountResponse = mockMvc.perform(post(CUENTAS_PATH)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(TestDataFactory.createCuentaRequestJson(
                                "AHORROS", java.math.BigDecimal.ZERO, false, clientId)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String numeroCuenta = accountResponse.substring(
                accountResponse.indexOf("\"numeroCuenta\":\"") + 16,
                accountResponse.indexOf("\"", accountResponse.indexOf("\"numeroCuenta\":\"") + 16)
        );

        mockMvc.perform(patch(CUENTAS_PATH + "/" + numeroCuenta + "/cancelar")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }
}
