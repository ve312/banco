package com.trinity.banco.component.cliente;

import com.trinity.banco.component.AbstractBaseIntegrationTest;
import com.trinity.banco.component.TestDataFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ClienteComponentTest extends AbstractBaseIntegrationTest {

    private static final String CLIENTES_PATH = "/clientes";

    @Test
    void shouldCreateClientWhenDataIsValid() throws Exception {
        String token = getAdminToken();

        mockMvc.perform(post(CLIENTES_PATH)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(TestDataFactory.createValidClienteRequest()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.tipoIdentificacion").value("CC"))
                .andExpect(jsonPath("$.numeroIdentificacion").value("1234567890"))
                .andExpect(jsonPath("$.nombres").value("Juan Carlos"))
                .andExpect(jsonPath("$.apellidos").value("Perez Lopez"))
                .andExpect(jsonPath("$.email").value("juan.perez@email.com"))
                .andExpect(jsonPath("$.fechaNacimiento").value("1990-05-15"))
                .andExpect(jsonPath("$.fechaCreacion").isNotEmpty())
                .andExpect(jsonPath("$.fechaModificacion").isNotEmpty());
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsDuplicated() throws Exception {
        String token = getAdminToken();

        mockMvc.perform(post(CLIENTES_PATH)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(TestDataFactory.createValidClienteRequest()))
                .andExpect(status().isCreated());

        String duplicateEmailRequest = TestDataFactory.createClienteRequestJson(
                "CC",
                "9876543210",
                "Maria",
                "Gomez",
                "juan.perez@email.com",
                LocalDate.of(1995, 3, 20)
        );

        mockMvc.perform(post(CLIENTES_PATH)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(duplicateEmailRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Un cliente con este email ya existe"));
    }

    @Test
    void shouldReturnClientWhenExists() throws Exception {
        String token = getAdminToken();

        String createResponse = mockMvc.perform(post(CLIENTES_PATH)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(TestDataFactory.createValidClienteRequest()))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long clientId = Long.parseLong(
                createResponse.substring(createResponse.indexOf("\"id\":") + 5,
                        createResponse.indexOf(",", createResponse.indexOf("\"id\":") + 5))
        );

        mockMvc.perform(get(CLIENTES_PATH + "/" + clientId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(clientId))
                .andExpect(jsonPath("$.numeroIdentificacion").value("1234567890"))
                .andExpect(jsonPath("$.nombres").value("Juan Carlos"));
    }

    @Test
    void shouldReturnBadRequestWhenDeletingClientWithAccounts() throws Exception {
        String token = getAdminToken();

        String createClientJson = TestDataFactory.createClienteRequestJson(
                "CC", "1112223334", "Pedro", "Ramirez",
                "pedro.ramirez@email.com", LocalDate.of(1988, 7, 10)
        );

        String clientResponse = mockMvc.perform(post(CLIENTES_PATH)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(createClientJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long clientId = Long.parseLong(
                clientResponse.substring(clientResponse.indexOf("\"id\":") + 5,
                        clientResponse.indexOf(",", clientResponse.indexOf("\"id\":") + 5))
        );

        mockMvc.perform(post("/cuentas")
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content(TestDataFactory.createSavingsAccountRequest(clientId)))
                .andExpect(status().isCreated());

        mockMvc.perform(delete(CLIENTES_PATH + "/" + clientId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("No se puede eliminar el cliente porque tiene cuentas asociadas"));
    }

    @Test
    void shouldReturnUnauthorizedWhenNoToken() throws Exception {
        mockMvc.perform(post(CLIENTES_PATH)
                        .contentType("application/json")
                        .content(TestDataFactory.createValidClienteRequest()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnForbiddenWhenAuditorCreatesClient() throws Exception {
        String auditorToken = getAuditorToken();

        mockMvc.perform(post(CLIENTES_PATH)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + auditorToken)
                        .content(TestDataFactory.createValidClienteRequest()))
                .andExpect(status().isForbidden());
    }
}
