package com.trinity.banco.application.validator;

public class ClienteValidator {
    public static void validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9-]+\\.[A-Za-z]{2,}$";
        if (email == null || !email.matches(regex)) {
            throw new RuntimeException("El campo email es obligatorio y debe tener un formato válido");
        }
    }

    public static void validarNombre(String nombre, String apellido) {
        if (nombre == null || nombre.trim().length() < 2) {
            throw new RuntimeException("El nombre debe tener al menos 2 caracteres");
        }
        if (apellido == null || apellido.trim().length() < 2) {
            throw new RuntimeException("El apellido debe tener al menos 2 caracteres");
        }
    }
}
