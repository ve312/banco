package com.trinity.banco.cliente.domain.model;

import com.trinity.banco.cliente.domain.model.enums.TipoIdentificacion;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class Cliente {
    private Long id;
    private TipoIdentificacion tipoIdentificacion;
    private String numeroIdentificacion;
    private String nombres;
    private String apellidos;
    private String email;
    private LocalDate fechaNacimiento;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;

    public Cliente(Long id,
                   TipoIdentificacion tipoIdentificacion,
                   String numeroIdentificacion,
                   String nombres,
                   String apellidos,
                   String email,
                   LocalDate fechaNacimiento,
                   LocalDateTime fechaCreacion,
                   LocalDateTime fechaModificacion) {
        this.id = id;
        this.tipoIdentificacion = tipoIdentificacion;
        this.numeroIdentificacion = numeroIdentificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaCreacion = fechaCreacion;
        this.fechaModificacion = fechaModificacion;
    }

    public void actualizarDatos(String nombres, String apellidos, String email) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.email = email;
        this.fechaModificacion = LocalDateTime.now();
    }

}
