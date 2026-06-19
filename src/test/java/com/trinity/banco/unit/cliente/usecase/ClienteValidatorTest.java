package com.trinity.banco.unit.cliente.usecase;

import com.trinity.banco.cliente.application.validators.ClienteValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteValidatorTest {

    @Test
    void deberia_aceptar_email_valido() {
        assertDoesNotThrow(() -> ClienteValidator.validarEmail("test@example.com"));
    }

    @Test
    void deberia_rechazar_email_nulo() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> ClienteValidator.validarEmail(null));
        assertEquals("El campo email es obligatorio y debe tener un formato válido", ex.getMessage());
    }

    @Test
    void deberia_rechazar_email_sin_arroba() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> ClienteValidator.validarEmail("invalido"));
        assertEquals("El campo email es obligatorio y debe tener un formato válido", ex.getMessage());
    }

    @Test
    void deberia_rechazar_email_sin_dominio() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> ClienteValidator.validarEmail("test@"));
        assertEquals("El campo email es obligatorio y debe tener un formato válido", ex.getMessage());
    }

    @Test
    void deberia_aceptar_nombre_valido() {
        assertDoesNotThrow(() -> ClienteValidator.validarNombre("Juan", "Perez"));
    }

    @Test
    void deberia_rechazar_nombre_corto() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> ClienteValidator.validarNombre("J", "Perez"));
        assertEquals("El nombre debe tener al menos 2 caracteres", ex.getMessage());
    }

    @Test
    void deberia_rechazar_apellido_corto() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> ClienteValidator.validarNombre("Juan", "P"));
        assertEquals("El apellido debe tener al menos 2 caracteres", ex.getMessage());
    }

    @Test
    void deberia_rechazar_nombre_nulo() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> ClienteValidator.validarNombre(null, "Perez"));
        assertEquals("El nombre debe tener al menos 2 caracteres", ex.getMessage());
    }

    @Test
    void deberia_rechazar_nombre_solo_espacios() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> ClienteValidator.validarNombre("   ", "Perez"));
        assertEquals("El nombre debe tener al menos 2 caracteres", ex.getMessage());
    }
}
