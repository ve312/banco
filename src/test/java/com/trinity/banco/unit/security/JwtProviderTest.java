package com.trinity.banco.unit.security;

import com.trinity.banco.shared.infrastructure.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtProviderTest {

    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider(
                "testSecretKeyForUnitTestingPurposesOnly12345678",
                86400000L
        );
    }

    @Test
    void deberia_generar_token_valido() {
        String token = jwtProvider.generarToken("admin", "ADMIN");

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    void deberia_validar_token_correcto() {
        String token = jwtProvider.generarToken("admin", "ADMIN");

        assertTrue(jwtProvider.validarToken(token));
    }

    @Test
    void deberia_extraer_username_de_token() {
        String token = jwtProvider.generarToken("jperez", "ASESOR");

        String username = jwtProvider.getUsernameFromToken(token);

        assertEquals("jperez", username);
    }

    @Test
    void deberia_extraer_rol_de_token() {
        String token = jwtProvider.generarToken("admin", "ADMIN");

        String rol = jwtProvider.getRolFromToken(token);

        assertEquals("ADMIN", rol);
    }

    @Test
    void deberia_rechazar_token_invalido() {
        boolean resultado = jwtProvider.validarToken("token-invalido");

        assertFalse(resultado);
    }

    @Test
    void deberia_rechazar_token_vacio() {
        boolean resultado = jwtProvider.validarToken("");

        assertFalse(resultado);
    }

    @Test
    void deberia_rechazar_token_nulo() {
        boolean resultado = jwtProvider.validarToken(null);

        assertFalse(resultado);
    }
}
