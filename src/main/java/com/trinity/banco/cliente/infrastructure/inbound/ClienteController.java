package com.trinity.banco.cliente.infrastructure.inbound;

import com.trinity.banco.cliente.application.usecases.*;
import com.trinity.banco.cliente.domain.model.Cliente;
import com.trinity.banco.cliente.infrastructure.inbound.dto.request.ActualizarClienteRequest;
import com.trinity.banco.cliente.infrastructure.inbound.dto.request.CrearClienteRequest;
import com.trinity.banco.cliente.infrastructure.inbound.dto.response.ClienteResponse;
import com.trinity.banco.cliente.infrastructure.inbound.mappers.ClienteMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private final CrearClienteUseCase crearClienteService;
    private final ActualizarClienteUseCase actualizarClienteService;
    private final EliminarClienteUseCase eliminarClienteService;
    private final ObtenerClienteUseCase obtenerClienteService;
    private final ListarClientesUseCase listarClientesService;

    public ClienteController(
            CrearClienteUseCase crearClienteService,
            ActualizarClienteUseCase actualizarClienteService,
            EliminarClienteUseCase eliminarClienteService,
            ObtenerClienteUseCase obtenerClienteService,
            ListarClientesUseCase listarClientesService
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
