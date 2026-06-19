package com.trinity.banco.usuario.domain.model;

import com.trinity.banco.usuario.domain.model.enums.Rol;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Usuario {
    private Long id;
    private String username;
    private String password;
    private String nombre;
    private String apellido;
    private boolean activo;
    private Rol rol;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    public Usuario(Long id, String username, String password, String nombre, String apellido, boolean activo, Rol rol, LocalDateTime fechaCreacion, LocalDateTime fechaModificacion) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.activo = activo;
        this.rol = rol;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    public void actualizarDatos(String nombre, String apellido, Rol rol, boolean activo) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.rol = rol;
        this.activo = activo;
        this.fechaModificacion = LocalDateTime.now();
    }

    public void actualizarPassword(String password) {
        this.password = password;
        this.fechaModificacion = LocalDateTime.now();
    }
}
