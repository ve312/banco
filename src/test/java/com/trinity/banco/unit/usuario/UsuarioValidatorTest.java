package com.trinity.banco.unit.usuario;

import com.trinity.banco.usuario.application.validators.UsuarioValidator;
import com.trinity.banco.usuario.domain.model.enums.Rol;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioValidatorTest {

    @Test
    void deberia_aceptar_username_valido() {
        assertDoesNotThrow(() -> UsuarioValidator.validarUsername("jperez"));
    }

    @Test
    void deberia_rechazar_username_corto() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> UsuarioValidator.validarUsername("jp"));
        assertEquals("El username debe tener al menos 3 caracteres", ex.getMessage());
    }

    @Test
    void deberia_rechazar_username_nulo() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> UsuarioValidator.validarUsername(null));
        assertEquals("El username debe tener al menos 3 caracteres", ex.getMessage());
    }

    @Test
    void deberia_rechazar_username_con_caracteres_especiales() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> UsuarioValidator.validarUsername("user@name"));
        assertEquals("El username solo puede contener letras, números y guiones bajos", ex.getMessage());
    }

    @Test
    void deberia_aceptar_password_valida() {
        assertDoesNotThrow(() -> UsuarioValidator.validarPassword("123456"));
    }

    @Test
    void deberia_rechazar_password_corta() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> UsuarioValidator.validarPassword("12345"));
        assertEquals("La contraseña debe tener al menos 6 caracteres", ex.getMessage());
    }

    @Test
    void deberia_rechazar_password_nula() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> UsuarioValidator.validarPassword(null));
        assertEquals("La contraseña debe tener al menos 6 caracteres", ex.getMessage());
    }

    @Test
    void deberia_aceptar_nombre_valido() {
        assertDoesNotThrow(() -> UsuarioValidator.validarNombre("Juan", "Perez"));
    }

    @Test
    void deberia_rechazar_nombre_vacio() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> UsuarioValidator.validarNombre("", "Perez"));
        assertEquals("El nombre es obligatorio", ex.getMessage());
    }

    @Test
    void deberia_rechazar_apellido_vacio() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> UsuarioValidator.validarNombre("Juan", ""));
        assertEquals("El apellido es obligatorio", ex.getMessage());
    }

    @Test
    void deberia_rechazar_nombre_solo_espacios() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> UsuarioValidator.validarNombre("   ", "Perez"));
        assertEquals("El nombre es obligatorio", ex.getMessage());
    }

    @Test
    void deberia_aceptar_rol_valido() {
        assertEquals(Rol.ADMIN, UsuarioValidator.validarRol("ADMIN"));
        assertEquals(Rol.ASESOR, UsuarioValidator.validarRol("asesor"));
        assertEquals(Rol.AUDITOR, UsuarioValidator.validarRol("Auditor"));
    }

    @Test
    void deberia_rechazar_rol_invalido() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> UsuarioValidator.validarRol("INVALIDO"));
        assertTrue(ex.getMessage().contains("Rol inválido"));
    }

    @Test
    void deberia_rechazar_rol_nulo() {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> UsuarioValidator.validarRol(null));
        assertTrue(ex.getMessage().contains("Rol inválido"));
    }
}
