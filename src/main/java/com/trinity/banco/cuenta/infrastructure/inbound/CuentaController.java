package com.trinity.banco.cuenta.infrastructure.inbound;

import com.trinity.banco.cuenta.application.usecases.*;
import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.cuenta.infrastructure.inbound.dto.request.CrearCuentaRequest;
import com.trinity.banco.cuenta.infrastructure.inbound.dto.response.CuentaResponse;
import com.trinity.banco.cuenta.infrastructure.outbound.mappers.CuentaMapper;
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

@Tag(name = "Cuentas")
@RestController
@RequestMapping("/cuentas")
@SecurityRequirement(name = "bearer-jwt")
public class CuentaController {
    private final CrearCuentaUseCase crearCuentaService;
    private final CancelarCuentaUseCase cancelarCuentaService;
    private final ActivarCuentaUseCase activarCuentaService;
    private final InactivarCuentaUseCase inactivarCuentaService;
    private final ObtenerCuentaUseCase obtenerCuentaService;
    private final ListarCuentasPorClienteUseCase listarPorClienteService;
    private final ListarTodasLasCuentasUseCase listarTodasLasCuentasService;

    public CuentaController(
            CrearCuentaUseCase crearCuentaService,
            CancelarCuentaUseCase cancelarCuentaService,
            ActivarCuentaUseCase activarCuentaService,
            InactivarCuentaUseCase inactivarCuentaService,
            ObtenerCuentaUseCase obtenerCuentaService,
            ListarCuentasPorClienteUseCase listarPorClienteService,
            ListarTodasLasCuentasUseCase listarTodasLasCuentasService
    ) {
        this.crearCuentaService = crearCuentaService;
        this.cancelarCuentaService = cancelarCuentaService;
        this.activarCuentaService = activarCuentaService;
        this.inactivarCuentaService = inactivarCuentaService;
        this.obtenerCuentaService = obtenerCuentaService;
        this.listarPorClienteService = listarPorClienteService;
        this.listarTodasLasCuentasService = listarTodasLasCuentasService;
    }


    @PostMapping
    @Operation(summary = "Crear una cuenta", description = "Crea una nueva cuenta bancaria asociada a un cliente existente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o cliente no existe")
    })
    public ResponseEntity<CuentaResponse> crear(
            @Valid @RequestBody CrearCuentaRequest request
    ) {

        Cuenta cuenta = crearCuentaService.ejecutar(
                CuentaMapper.toTipoCuenta(request.getTipoCuenta()),
                request.getSaldoInicial(),
                request.isExentaGMF(),
                request.getClienteId()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CuentaMapper.toResponse(cuenta));
    }


    @GetMapping
    @Operation(summary = "Listar todas las cuentas", description = "Retorna todas las cuentas bancarias registradas en el sistema")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de cuentas obtenida exitosamente")
    })
    public ResponseEntity<List<CuentaResponse>> listarTodas() {
        List<CuentaResponse> response = listarTodasLasCuentasService.ejecutar()
                .stream()
                .map(CuentaMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{numeroCuenta}")
    @Operation(summary = "Obtener cuenta por número", description = "Retorna los detalles de una cuenta bancaria dado su número de cuenta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta encontrada"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    public ResponseEntity<CuentaResponse> obtener(
            @PathVariable String numeroCuenta
    ) {

        Cuenta cuenta = obtenerCuentaService.ejecutar(numeroCuenta);

        return ResponseEntity.ok(
                CuentaMapper.toResponse(cuenta)
        );
    }


    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar cuentas por cliente", description = "Retorna todas las cuentas bancarias asociadas a un cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de cuentas obtenida exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<List<CuentaResponse>> listarPorCliente(
            @PathVariable Long clienteId
    ) {

        List<CuentaResponse> response = listarPorClienteService.ejecutar(clienteId)
                .stream()
                .map(CuentaMapper::toResponse)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{numeroCuenta}/cancelar")
    @Operation(summary = "Cancelar cuenta", description = "Cambia el estado de una cuenta a CANCELADA. No se permiten más operaciones sobre ella")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cuenta cancelada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    public ResponseEntity<Void> cancelar(
            @PathVariable String numeroCuenta
    ) {

        cancelarCuentaService.ejecutar(numeroCuenta);

        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{numeroCuenta}/activar")
    @Operation(summary = "Activar cuenta", description = "Cambia el estado de una cuenta a ACTIVA, permitiendo realizar operaciones sobre ella")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cuenta activada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    public ResponseEntity<Void> activar(
            @PathVariable String numeroCuenta
    ) {

        activarCuentaService.ejecutar(numeroCuenta);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{numeroCuenta}/inactivar")
    @Operation(summary = "Inactivar cuenta", description = "Cambia el estado de una cuenta a INACTIVA. La cuenta existe pero no puede realizar transacciones")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cuenta inactivada exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    public ResponseEntity<Void> inactivar(
            @PathVariable String numeroCuenta
    ) {

        inactivarCuentaService.ejecutar(numeroCuenta);

        return ResponseEntity.noContent().build();
    }
}
