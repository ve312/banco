package com.trinity.banco.unit.shared;

import com.trinity.banco.shared.domain.errors.ApiError;
import com.trinity.banco.shared.domain.errors.RecursoNoEncontradoException;
import com.trinity.banco.shared.infrastructure.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void deberia_manejar_not_found() {
        RecursoNoEncontradoException ex = new RecursoNoEncontradoException("Recurso no encontrado");

        ResponseEntity<ApiError> response = handler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Recurso no encontrado", response.getBody().getMessage());
        assertEquals(404, response.getBody().getStatus());
    }

    @Test
    void deberia_manejar_bad_credentials() {
        BadCredentialsException ex = new BadCredentialsException("Bad credentials");

        ResponseEntity<ApiError> response = handler.handleBadCredentials(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("Credenciales inválidas", response.getBody().getMessage());
    }

    @Test
    void deberia_manejar_authentication_error() {
        AuthenticationException ex = new AuthenticationException("Token expirado") {};

        ResponseEntity<ApiError> response = handler.handleAuthentication(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("Token expirado"));
    }

    @Test
    void deberia_manejar_access_denied() {
        AccessDeniedException ex = new AccessDeniedException("Acceso denegado");

        ResponseEntity<ApiError> response = handler.handleAccessDenied(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("Acceso denegado", response.getBody().getMessage());
    }

    @Test
    void deberia_manejar_runtime_exception() {
        RuntimeException ex = new RuntimeException("Error de negocio");

        ResponseEntity<ApiError> response = handler.handleRuntimeException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error de negocio", response.getBody().getMessage());
    }

    @Test
    void deberia_manejar_exception_general() {
        Exception ex = new Exception("Error interno");

        ResponseEntity<ApiError> response = handler.handleGeneralException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor", response.getBody().getMessage());
    }

    @Test
    void deberia_manejar_type_mismatch_sin_tipo() {
        var ex = new org.springframework.web.method.annotation.MethodArgumentTypeMismatchException(
                "texto", null, "id", null, null
        );

        ResponseEntity<ApiError> response = handler.handleTypeMismatch(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El parámetro id debe ser un válido", response.getBody().getMessage());
    }

    @Test
    void deberia_manejar_type_mismatch_con_tipo_conocido() {
        var ex = new org.springframework.web.method.annotation.MethodArgumentTypeMismatchException(
                "texto", Integer.class, "id", null, null
        );

        ResponseEntity<ApiError> response = handler.handleTypeMismatch(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El parámetro id debe ser un Integer", response.getBody().getMessage());
    }
}
