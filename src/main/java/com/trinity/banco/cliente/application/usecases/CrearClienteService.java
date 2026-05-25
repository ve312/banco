package com.trinity.banco.cliente.application.usecases;

import com.trinity.banco.cliente.application.validators.ClienteValidator;
import com.trinity.banco.cliente.domain.model.Cliente;
import com.trinity.banco.cliente.domain.model.enums.TipoIdentificacion;
import com.trinity.banco.cliente.domain.ports.ClienteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class CrearClienteService {

    private final ClienteRepository clienteRepository;

    public CrearClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente ejecutar(TipoIdentificacion tipoIdentificacion,
                            String numeroIdentificacion,
                            String nombres,
                            String apellidos,
                            String email,
                            LocalDate fechaNacimiento) {

        if (clienteRepository.existePorNumeroIdentificacion(numeroIdentificacion)) {
            throw new RuntimeException("El cliente ya existe");
        }

        if (!fechaNacimiento.isBefore(LocalDate.now().minusYears(18))) {
            throw new RuntimeException("El cliente debe ser mayor de edad");
        }
        if (fechaNacimiento.isBefore(LocalDate.now().minusYears(120))) {
            throw new RuntimeException("Edad no válida");
        }

        ClienteValidator.validarEmail(email);
        ClienteValidator.validarNombre(nombres,  apellidos);

        Cliente cliente = new Cliente(
                null,
                tipoIdentificacion,
                numeroIdentificacion,
                nombres,
                apellidos,
                email,
                fechaNacimiento,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        return clienteRepository.guardar(cliente);
    }
}
