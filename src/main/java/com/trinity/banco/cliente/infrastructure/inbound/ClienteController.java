package com.trinity.banco.cliente.infrastructure.inbound;

import com.trinity.banco.cliente.application.usecases.*;
import com.trinity.banco.cliente.domain.model.Cliente;
import com.trinity.banco.cliente.infrastructure.inbound.dto.request.ActualizarClienteRequest;
import com.trinity.banco.cliente.infrastructure.inbound.dto.request.CrearClienteRequest;
import com.trinity.banco.cliente.infrastructure.inbound.dto.response.ClienteResponse;
import com.trinity.banco.cliente.infrastructure.inbound.mappers.ClienteMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Clientes")
@RestController
@RequestMapping("/clientes")
@SecurityRequirement(name = "bearer-jwt")
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
    @Operation(summary = "Crear un cliente", description = "Registra un nuevo cliente en el sistema con los datos proporcionados")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
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
    @Operation(summary = "Actualizar un cliente", description = "Actualiza los datos de un cliente existente identificado por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
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
    @Operation(summary = "Eliminar un cliente", description = "Elimina un cliente del sistema por su ID. Si el cliente tiene cuentas activas, no se permitirá la eliminación")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente"),
            @ApiResponse(responseCode = "400", description = "El cliente tiene cuentas asociadas activas"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        eliminarClienteService.ejecutar(id);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{id}")
    @Operation(summary = "Obtener un cliente por ID", description = "Retorna los datos de un cliente específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<ClienteResponse> obtener(@PathVariable Long id) {

        Cliente cliente = obtenerClienteService.ejecutar(id);

        return ResponseEntity.ok(
                ClienteMapper.toResponse(cliente)
        );
    }

    @GetMapping
    @Operation(summary = "Listar todos los clientes", description = "Retorna una lista con todos los clientes registrados en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente")
    })
    public ResponseEntity<List<ClienteResponse>> listar() {

        List<ClienteResponse> response = listarClientesService.ejecutar()
                .stream()
                .map(ClienteMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
