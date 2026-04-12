package com.trinity.banco.rest.controller;

import com.trinity.banco.application.service.cliente.*;
import com.trinity.banco.domain.model.Cliente;
import com.trinity.banco.rest.dto.request.ActualizarClienteRequest;
import com.trinity.banco.rest.dto.request.CrearClienteRequest;
import com.trinity.banco.rest.dto.responses.ClienteResponse;
import com.trinity.banco.rest.mapper.ClienteMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final CrearClienteService crearClienteService;
    private final ActualizarClienteService actualizarClienteService;
    private final EliminarClienteService eliminarClienteService;
    private final ObtenerClienteService obtenerClienteService;
    private final ListarClientesService listarClientesService;

    public ClienteController(
            CrearClienteService crearClienteService,
            ActualizarClienteService actualizarClienteService,
            EliminarClienteService eliminarClienteService,
            ObtenerClienteService obtenerClienteService,
            ListarClientesService listarClientesService
    ) {
        this.crearClienteService = crearClienteService;
        this.actualizarClienteService = actualizarClienteService;
        this.eliminarClienteService = eliminarClienteService;
        this.obtenerClienteService = obtenerClienteService;
        this.listarClientesService = listarClientesService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> crear(@Valid @RequestBody CrearClienteRequest request) {

        Cliente cliente = crearClienteService.ejecutar(
                ClienteMapper.toTipoIdentificacion(request.getTipoIdentificacion()),
                request.getNumeroIdentificacion(),
                request.getNombres(),
                request.getApellidos(),
                request.getEmail(),
                request.getFechaNacimiento()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.toResponse(cliente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarClienteRequest request
    ) {

        Cliente cliente = actualizarClienteService.ejecutar(
                id,
                request.getNombres(),
                request.getApellidos(),
                request.getEmail()
        );

        return ResponseEntity.ok(
                ClienteMapper.toResponse(cliente)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        eliminarClienteService.ejecutar(id);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> obtener(@PathVariable Long id) {

        Cliente cliente = obtenerClienteService.ejecutar(id);

        return ResponseEntity.ok(
                ClienteMapper.toResponse(cliente)
        );
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {

        List<ClienteResponse> response = listarClientesService.ejecutar()
                .stream()
                .map(ClienteMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
