package com.trinity.banco.usuario.infrastructure.inbound;

import com.trinity.banco.usuario.application.usecases.*;
import com.trinity.banco.usuario.domain.model.Usuario;
import com.trinity.banco.usuario.infrastructure.inbound.dto.request.ActualizarUsuarioRequest;
import com.trinity.banco.usuario.infrastructure.inbound.dto.request.CambiarPasswordRequest;
import com.trinity.banco.usuario.infrastructure.inbound.dto.request.CrearUsuarioRequest;
import com.trinity.banco.usuario.infrastructure.inbound.dto.response.UsuarioResponse;
import com.trinity.banco.usuario.infrastructure.inbound.mappers.UsuarioMapper;
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

@Tag(name = "Usuarios")
@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-jwt")
public class UsuarioController {
    private final CrearUsuarioUseCase crearUsuarioService;
    private final ActualizarUsuarioUseCase actualizarUsuarioService;
    private final EliminarUsuarioUseCase eliminarUsuarioService;
    private final ObtenerUsuarioUseCase obtenerUsuarioService;
    private final ListarUsuariosUseCase listarUsuariosService;
    private final CambiarPasswordUseCase cambiarPasswordService;

    public UsuarioController(
            CrearUsuarioUseCase crearUsuarioService,
            ActualizarUsuarioUseCase actualizarUsuarioService,
            EliminarUsuarioUseCase eliminarUsuarioService,
            ObtenerUsuarioUseCase obtenerUsuarioService,
            ListarUsuariosUseCase listarUsuariosService,
            CambiarPasswordUseCase cambiarPasswordService
    ) {
        this.crearUsuarioService = crearUsuarioService;
        this.actualizarUsuarioService = actualizarUsuarioService;
        this.eliminarUsuarioService = eliminarUsuarioService;
        this.obtenerUsuarioService = obtenerUsuarioService;
        this.listarUsuariosService = listarUsuariosService;
        this.cambiarPasswordService = cambiarPasswordService;
    }

    @PostMapping
    @Operation(summary = "Crear un usuario", description = "Registra un nuevo empleado en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o username duplicado")
    })
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody CrearUsuarioRequest request) {
        Usuario usuario = crearUsuarioService.ejecutar(
                request.getUsername(),
                request.getPassword(),
                request.getNombre(),
                request.getApellido(),
                UsuarioMapper.toRol(request.getRol())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toResponse(usuario));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un usuario", description = "Actualiza los datos de un usuario existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UsuarioResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarUsuarioRequest request
    ) {
        Usuario usuario = actualizarUsuarioService.ejecutar(
                id,
                request.getNombre(),
                request.getApellido(),
                UsuarioMapper.toRol(request.getRol()),
                request.isActivo()
        );

        return ResponseEntity.ok(UsuarioMapper.toResponse(usuario));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un usuario", description = "Elimina un usuario del sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eliminarUsuarioService.ejecutar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un usuario por ID", description = "Retorna los datos de un usuario específico")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UsuarioResponse> obtener(@PathVariable Long id) {
        Usuario usuario = obtenerUsuarioService.ejecutar(id);
        return ResponseEntity.ok(UsuarioMapper.toResponse(usuario));
    }

    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Retorna todos los usuarios registrados en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida exitosamente")
    })
    public ResponseEntity<List<UsuarioResponse>> listar() {
        List<UsuarioResponse> response = listarUsuariosService.ejecutar()
                .stream()
                .map(UsuarioMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/password")
    @Operation(summary = "Cambiar contraseña", description = "Cambia la contraseña de un usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Contraseña actualizada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    public ResponseEntity<UsuarioResponse> cambiarPassword(
            @PathVariable Long id,
            @Valid @RequestBody CambiarPasswordRequest request
    ) {
        Usuario usuario = cambiarPasswordService.ejecutar(id, request.getNuevaPassword());
        return ResponseEntity.ok(UsuarioMapper.toResponse(usuario));
    }
}
