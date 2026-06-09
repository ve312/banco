package com.trinity.banco.transaccion.infrastructure.inbound;

import com.trinity.banco.transaccion.application.usecases.ConsignarUseCase;
import com.trinity.banco.transaccion.application.usecases.ListarTransaccionesPorCuentaUseCase;
import com.trinity.banco.transaccion.application.usecases.RetirarUseCase;
import com.trinity.banco.transaccion.application.usecases.TransferirUseCase;
import com.trinity.banco.transaccion.domain.model.Transaccion;
import com.trinity.banco.transaccion.infrastructure.inbound.dto.request.MovimientoRequest;
import com.trinity.banco.transaccion.infrastructure.inbound.dto.request.TransferenciaRequest;
import com.trinity.banco.transaccion.infrastructure.inbound.dto.response.TransaccionResponse;
import com.trinity.banco.transaccion.infrastructure.inbound.mappers.TransaccionMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Transacciones")
@RestController
@RequestMapping("/transacciones")
public class TransaccionController {
    private final ConsignarUseCase consignarService;
    private final RetirarUseCase retirarService;
    private final TransferirUseCase transferirService;
    private final ListarTransaccionesPorCuentaUseCase listarService;

    public TransaccionController(
            ConsignarUseCase consignarService,
            RetirarUseCase retirarService,
            TransferirUseCase transferirService,
            ListarTransaccionesPorCuentaUseCase listarService
    ) {
        this.consignarService = consignarService;
        this.retirarService = retirarService;
        this.transferirService = transferirService;
        this.listarService = listarService;
    }


    @PostMapping("/consignar")
    @Operation(summary = "Consignar dinero", description = "Realiza un depósito en una cuenta bancaria, incrementando su saldo")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consignación realizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos o monto inválido"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    public ResponseEntity<TransaccionResponse> consignar(
            @Valid @RequestBody MovimientoRequest request
    ) {

        Transaccion transaccion = consignarService.ejecutar(
                request.getNumeroCuenta(),
                request.getMonto()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(TransaccionMapper.toResponse(transaccion));
    }


    @PostMapping("/retirar")
    @Operation(summary = "Retirar dinero", description = "Realiza un retiro de una cuenta bancaria, decrementando su saldo si hay fondos suficientes")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retiro realizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o saldo insuficiente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    public ResponseEntity<TransaccionResponse> retirar(
            @Valid @RequestBody MovimientoRequest request
    ) {

        Transaccion transaccion = retirarService.ejecutar(
                request.getNumeroCuenta(),
                request.getMonto()
        );

        return ResponseEntity.ok(
                TransaccionMapper.toResponse(transaccion)
        );
    }


    @PostMapping("/transferir")
    @Operation(summary = "Transferir entre cuentas", description = "Transfiere un monto entre dos cuentas bancarias. Genera dos transacciones: una de egreso y una de ingreso")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Transferencia realizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos o saldo insuficiente"),
            @ApiResponse(responseCode = "404", description = "Cuenta origen o destino no encontrada")
    })
    public ResponseEntity<List<TransaccionResponse>> transferir(
            @Valid @RequestBody TransferenciaRequest request
    ) {

        List<Transaccion> transacciones = transferirService.ejecutar(
                request.getCuentaOrigenNumero(),
                request.getCuentaDestinoNumero(),
                request.getMonto()
        );

        List<TransaccionResponse> response = transacciones.stream()
                .map(TransaccionMapper::toResponse)
                .toList();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/cuenta/{numeroCuenta}")
    @Operation(summary = "Listar transacciones por cuenta", description = "Retorna el historial de transacciones realizadas en una cuenta específica")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Historial de transacciones obtenido exitosamente"),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    public ResponseEntity<List<TransaccionResponse>> listarPorCuenta(
            @PathVariable String numeroCuenta
    ) {

        List<Transaccion> transacciones = listarService.ejecutar(numeroCuenta);

        List<TransaccionResponse> response = transacciones.stream()
                .map(TransaccionMapper::toResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

}
