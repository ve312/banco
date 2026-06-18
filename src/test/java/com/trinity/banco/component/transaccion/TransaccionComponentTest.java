package com.trinity.banco.component.transaccion;

import com.trinity.banco.component.AbstractBaseIntegrationTest;
import com.trinity.banco.component.TestDataFactory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TransaccionComponentTest extends AbstractBaseIntegrationTest {

    private static final String CLIENTES_PATH = "/clientes";
    private static final String CUENTAS_PATH = "/cuentas";
    private static final String TRANSACCIONES_PATH = "/transacciones";

    private static final BigDecimal GMF_RATE = new BigDecimal("0.004");
    private static int clientCounter = 0;

    private static class TestAccount {
        final Long clientId;
        final String numeroCuenta;
        final BigDecimal saldoInicial;

        TestAccount(Long clientId, String numeroCuenta, BigDecimal saldoInicial) {
            this.clientId = clientId;
            this.numeroCuenta = numeroCuenta;
            this.saldoInicial = saldoInicial;
        }
    }

    private Long createTestClient(String token, String suffix) throws Exception {
        clientCounter++;
        String apellido = "Test" + suffix.replaceAll("[^A-Za-z]", "");
        String numeroIdent = String.format("400000%04d", clientCounter);
        String json = TestDataFactory.createClienteRequestJson(
                "CC",
                numeroIdent,
                "Cliente",
                apellido.isEmpty() ? "Test" : apellido,
                "cliente." + suffix + "@test.com",
                LocalDate.of(1990, 1, 1)
        );
        String response = mockMvc.perform(post(CLIENTES_PATH)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return Long.parseLong(response.substring(
                response.indexOf("\"id\":") + 5,
                response.indexOf(",", response.indexOf("\"id\":") + 5)
        ));
    }

    private TestAccount createAccount(String token, String tipo, BigDecimal saldo,
                                       boolean exentaGMF, Long clientId,
                                       String suffix) throws Exception {
        String json = TestDataFactory.createCuentaRequestJson(tipo, saldo, exentaGMF, clientId);
        String response = mockMvc.perform(post(CUENTAS_PATH)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String numeroCuenta = response.substring(
                response.indexOf("\"numeroCuenta\":\"") + 16,
                response.indexOf("\"", response.indexOf("\"numeroCuenta\":\"") + 16)
        );
        return new TestAccount(clientId, numeroCuenta, saldo);
    }

    @Test
    void shouldUpdateBalanceWhenConsigning() throws Exception {
        String token = getAdminToken();
        Long clientId = createTestClient(token, "consig01");
        TestAccount account = createAccount(token, "AHORROS", new BigDecimal("1000.00"), false, clientId, "consig01");

        BigDecimal depositAmount = new BigDecimal("500.00");

        mockMvc.perform(post(TRANSACCIONES_PATH + "/consignar")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(TestDataFactory.createMovimientoRequestJson(account.numeroCuenta, depositAmount)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCuenta").value(account.numeroCuenta))
                .andExpect(jsonPath("$.tipoTransaccion").value("CONSIGNACION"))
                .andExpect(jsonPath("$.monto").value(depositAmount.doubleValue()))
                .andExpect(jsonPath("$.saldoAnterior").value(1000.00))
                .andExpect(jsonPath("$.saldoPosterior").value(1500.00))
                .andExpect(jsonPath("$.impuesto").value(0.00));

        mockMvc.perform(get(CUENTAS_PATH + "/" + account.numeroCuenta)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(1500.00));
    }

    @Test
    void shouldRejectWithdrawalWhenInsufficientBalanceForSavings() throws Exception {
        String token = getAdminToken();
        Long clientId = createTestClient(token, "retiro01");
        TestAccount account = createAccount(token, "AHORROS", new BigDecimal("500.00"), false, clientId, "retiro01");

        BigDecimal withdrawalAmount = new BigDecimal("600.00");

        mockMvc.perform(post(TRANSACCIONES_PATH + "/retirar")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(TestDataFactory.createMovimientoRequestJson(account.numeroCuenta, withdrawalAmount)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Cuenta de ahorros no puede quedar en saldo negativo"));

        mockMvc.perform(get(CUENTAS_PATH + "/" + account.numeroCuenta)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(500.00));
    }

    @Test
    void shouldTransferWithoutGMFWhenSameClient() throws Exception {
        String token = getAdminToken();
        Long clientId = createTestClient(token, "samel01");

        TestAccount origin = createAccount(token, "AHORROS", new BigDecimal("1000.00"), false, clientId, "samel01o");
        TestAccount destination = createAccount(token, "AHORROS", new BigDecimal("500.00"), false, clientId, "samel01d");

        BigDecimal transferAmount = new BigDecimal("300.00");

        mockMvc.perform(post(TRANSACCIONES_PATH + "/transferir")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(TestDataFactory.createTransferenciaRequestJson(
                                origin.numeroCuenta, destination.numeroCuenta, transferAmount)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].numeroCuenta").value(origin.numeroCuenta))
                .andExpect(jsonPath("$[0].monto").value(transferAmount.doubleValue()))
                .andExpect(jsonPath("$[0].saldoAnterior").value(1000.00))
                .andExpect(jsonPath("$[0].saldoPosterior").value(700.00))
                .andExpect(jsonPath("$[0].impuesto").value(0.00))
                .andExpect(jsonPath("$[1].numeroCuenta").value(destination.numeroCuenta))
                .andExpect(jsonPath("$[1].monto").value(transferAmount.doubleValue()))
                .andExpect(jsonPath("$[1].saldoAnterior").value(500.00))
                .andExpect(jsonPath("$[1].saldoPosterior").value(800.00))
                .andExpect(jsonPath("$[1].impuesto").value(0.00));

        mockMvc.perform(get(CUENTAS_PATH + "/" + origin.numeroCuenta)
                        .header("Authorization", "Bearer " + token))
                .andExpect(jsonPath("$.saldo").value(700.00));

        mockMvc.perform(get(CUENTAS_PATH + "/" + destination.numeroCuenta)
                        .header("Authorization", "Bearer " + token))
                .andExpect(jsonPath("$.saldo").value(800.00));
    }

    @Test
    void shouldTransferWithGMFWhenDifferentClients() throws Exception {
        String token = getAdminToken();
        Long clientId1 = createTestClient(token, "diffc01a");
        Long clientId2 = createTestClient(token, "diffc01b");

        TestAccount origin = createAccount(token, "AHORROS", new BigDecimal("200000.00"), false,
                clientId1, "diffc01o");
        TestAccount destination = createAccount(token, "AHORROS", new BigDecimal("100000.00"), false,
                clientId2, "diffc01d");

        BigDecimal transferAmount = new BigDecimal("100000.00");
        BigDecimal expectedGmf = transferAmount.multiply(GMF_RATE).setScale(2, java.math.RoundingMode.HALF_UP);
        BigDecimal totalDebit = transferAmount.add(expectedGmf);

        mockMvc.perform(post(TRANSACCIONES_PATH + "/transferir")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(TestDataFactory.createTransferenciaRequestJson(
                                origin.numeroCuenta, destination.numeroCuenta, transferAmount)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].numeroCuenta").value(origin.numeroCuenta))
                .andExpect(jsonPath("$[0].monto").value(transferAmount.doubleValue()))
                .andExpect(jsonPath("$[0].saldoAnterior").value(200000.00))
                .andExpect(jsonPath("$[0].saldoPosterior")
                        .value(200000.00 - totalDebit.doubleValue()))
                .andExpect(jsonPath("$[0].impuesto").value(expectedGmf.doubleValue()))
                .andExpect(jsonPath("$[1].numeroCuenta").value(destination.numeroCuenta))
                .andExpect(jsonPath("$[1].monto").value(transferAmount.doubleValue()))
                .andExpect(jsonPath("$[1].saldoAnterior").value(100000.00))
                .andExpect(jsonPath("$[1].saldoPosterior").value(200000.00))
                .andExpect(jsonPath("$[1].impuesto").value(0.00));

        mockMvc.perform(get(CUENTAS_PATH + "/" + origin.numeroCuenta)
                        .header("Authorization", "Bearer " + token))
                .andExpect(jsonPath("$.saldo").value(200000.00 - totalDebit.doubleValue()));

        mockMvc.perform(get(CUENTAS_PATH + "/" + destination.numeroCuenta)
                        .header("Authorization", "Bearer " + token))
                .andExpect(jsonPath("$.saldo").value(200000.00));
    }

    @Test
    void shouldListTransactionsByAccount() throws Exception {
        String token = getAdminToken();
        Long clientId = createTestClient(token, "listtx01");
        TestAccount account = createAccount(token, "AHORROS", new BigDecimal("1000.00"), false, clientId, "listtx01");

        mockMvc.perform(post(TRANSACCIONES_PATH + "/consignar")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(TestDataFactory.createMovimientoRequestJson(
                                account.numeroCuenta, new BigDecimal("500.00"))))
                .andExpect(status().isCreated());

        mockMvc.perform(get(TRANSACCIONES_PATH + "/cuenta/" + account.numeroCuenta)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].tipoTransaccion").value("CONSIGNACION"))
                .andExpect(jsonPath("$[0].saldoAnterior").value(1000.00))
                .andExpect(jsonPath("$[0].saldoPosterior").value(1500.00));
    }
}
