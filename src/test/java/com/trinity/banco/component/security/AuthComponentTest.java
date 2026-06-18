package com.trinity.banco.component.security;

import com.trinity.banco.component.BaseComponentTest;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthComponentTest extends BaseComponentTest {

    private static final String AUTH_PATH = "/auth/login";

    @Test
    void shouldReturnTokenWhenCredentialsAreValid() throws Exception {
        String username = "valid_user";
        String password = "ValidPass123";
        String token = authenticateAndGetToken(username, password, com.trinity.banco.usuario.domain.model.enums.Rol.ADMIN);
        org.junit.jupiter.api.Assertions.assertNotNull(token);
        org.junit.jupiter.api.Assertions.assertFalse(token.isBlank());
    }

    @Test
    void shouldReturnUnauthorizedWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(post(AUTH_PATH)
                        .contentType("application/json")
                        .content("""
                                {
                                    "username": "nonexistent",
                                    "password": "SomePass123"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Credenciales inválidas"));
    }

    @Test
    void shouldReturnUnauthorizedWhenPasswordIsIncorrect() throws Exception {
        String username = "wrong_pass_user";
        String password = "CorrectPass123";
        authenticateAndGetToken(username, password, com.trinity.banco.usuario.domain.model.enums.Rol.ADMIN);

        mockMvc.perform(post(AUTH_PATH)
                        .contentType("application/json")
                        .content("""
                                {
                                    "username": "%s",
                                    "password": "WrongPass456"
                                }
                                """.formatted(username)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Credenciales inválidas"));
    }

    @Test
    void shouldReturnTokenWithCorrectRole() throws Exception {
        String username = "role_test_user";
        String password = "RoleTest123";
        authenticateAndGetToken(username, password, com.trinity.banco.usuario.domain.model.enums.Rol.ADMIN);

        mockMvc.perform(post(AUTH_PATH)
                        .contentType("application/json")
                        .content("""
                                {
                                    "username": "%s",
                                    "password": "%s"
                                }
                                """.formatted(username, password)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.rol").value("ADMIN"));
    }
}
