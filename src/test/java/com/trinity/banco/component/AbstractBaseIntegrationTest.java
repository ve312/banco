package com.trinity.banco.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.trinity.banco.AbstractContainerConfig;
import com.trinity.banco.usuario.domain.model.Usuario;
import com.trinity.banco.usuario.domain.model.enums.Rol;
import com.trinity.banco.usuario.domain.ports.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public abstract class AbstractBaseIntegrationTest extends AbstractContainerConfig {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    protected final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @SuppressWarnings("unchecked")
    protected String authenticateAndGetToken(String username, String rawPassword, Rol rol) {
        String encodedPassword = passwordEncoder.encode(rawPassword);

        Usuario usuario = new Usuario(
                null,
                username,
                encodedPassword,
                "Test",
                "User",
                true,
                rol,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        usuarioRepository.guardar(usuario);

        try {
            MvcResult result = mockMvc.perform(post("/auth/login")
                            .contentType("application/json")
                            .content("""
                                    {
                                        "username": "%s",
                                        "password": "%s"
                                    }
                                    """.formatted(username, rawPassword)))
                    .andExpect(status().isOk())
                    .andReturn();

            String json = result.getResponse().getContentAsString();
            Map<String, Object> response = objectMapper.readValue(json, Map.class);
            return (String) response.get("token");

        } catch (Exception e) {
            throw new RuntimeException("Failed to authenticate test user", e);
        }
    }

    protected String getAdminToken() {
        return authenticateAndGetToken("admin_test", "Admin123*", Rol.ADMIN);
    }

    protected String getAsesorToken() {
        return authenticateAndGetToken("asesor_test", "Asesor123*", Rol.ASESOR);
    }

    protected String getAuditorToken() {
        return authenticateAndGetToken("auditor_test", "Auditor123*", Rol.AUDITOR);
    }
}
