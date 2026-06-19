package com.trinity.banco.component.usuario;

import com.trinity.banco.component.AbstractBaseIntegrationTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UsuarioComponentTest extends AbstractBaseIntegrationTest {

    private static final String USUARIOS_PATH = "/usuarios";

    @Test
    void shouldCreateUserWhenDataIsValid() throws Exception {
        String token = getAdminToken();

        mockMvc.perform(post(USUARIOS_PATH)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content("""
                                {
                                    "username": "nuevo_asesor",
                                    "password": "AsesorPass1",
                                    "nombre": "Carlos",
                                    "apellido": "Martinez",
                                    "rol": "ASESOR"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("nuevo_asesor"))
                .andExpect(jsonPath("$.nombre").value("Carlos"))
                .andExpect(jsonPath("$.apellido").value("Martinez"))
                .andExpect(jsonPath("$.rol").value("ASESOR"))
                .andExpect(jsonPath("$.activo").value(true));
    }

    @Test
    void shouldReturnForbiddenWhenNonAdminCreatesUser() throws Exception {
        String asesorToken = getAsesorToken();

        mockMvc.perform(post(USUARIOS_PATH)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + asesorToken)
                        .content("""
                                {
                                    "username": "otro_usuario",
                                    "password": "Pass12345",
                                    "nombre": "Test",
                                    "apellido": "User",
                                    "rol": "AUDITOR"
                                }
                                """))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldListUsersWhenAdmin() throws Exception {
        String token = getAdminToken();

        mockMvc.perform(get(USUARIOS_PATH)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[?(@.username == 'admin_test')].username").exists());
    }

    @Test
    void shouldUpdateUserWhenAdmin() throws Exception {
        String token = getAdminToken();

        String response = mockMvc.perform(get(USUARIOS_PATH)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        @SuppressWarnings("unchecked")
        Long userId = ((Number) ((java.util.List<Map<String, Object>>)
                objectMapper.readValue(response, java.util.List.class)).get(0).get("id")).longValue();

        mockMvc.perform(put(USUARIOS_PATH + "/" + userId)
                        .contentType("application/json")
                        .header("Authorization", "Bearer " + token)
                        .content("""
                                {
                                    "nombre": "AdminActualizado",
                                    "apellido": "Sistema",
                                    "rol": "ADMIN",
                                    "activo": true
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("AdminActualizado"))
                .andExpect(jsonPath("$.apellido").value("Sistema"));
    }
}
