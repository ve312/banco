package com.trinity.banco.usuario.application.validators;

import com.trinity.banco.usuario.domain.model.enums.Rol;

public class UsuarioValidator {
    private UsuarioValidator() {}

    public static void validarUsername(String username) {
        if (username == null || username.trim().length() < 3) {
            throw new RuntimeException("El username debe tener al menos 3 caracteres");
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new RuntimeException("El username solo puede contener letras, números y guiones bajos");
        }
    }

    public static void validarPassword(String password) {
        if (password == null || password.length() < 6) {
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres");
        }
    }

    public static void validarNombre(String nombre, String apellido) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new RuntimeException("El nombre es obligatorio");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new RuntimeException("El apellido es obligatorio");
        }
    }

    public static Rol validarRol(String rol) {
        try {
            return Rol.valueOf(rol.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new RuntimeException("Rol inválido: " + rol + ". Los roles válidos son: ADMIN, ASESOR, AUDITOR");
        }
    }
}
