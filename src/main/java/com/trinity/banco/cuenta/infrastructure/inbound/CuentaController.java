package com.trinity.banco.rest.controller;

import com.trinity.banco.cuenta.application.usecases.*;
import com.trinity.banco.cuenta.domain.model.Cuenta;
import com.trinity.banco.rest.dto.request.CrearCuentaRequest;
import com.trinity.banco.rest.dto.responses.CuentaResponse;
import com.trinity.banco.rest.mapper.CuentaMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cuentas")
public class CuentaController {
    private final CrearCuentaService crearCuentaService;
    private final CancelarCuentaService cancelarCuentaService;
    private final ActivarCuentaService activarCuentaService;
    private final InactivarCuentaService inactivarCuentaService;
    private final ObtenerCuentaService obtenerCuentaService;
    private final ListarCuentasPorClienteService listarPorClienteService;

    public CuentaController(
            CrearCuentaService crearCuentaService,
            CancelarCuentaService cancelarCuentaService,
            ActivarCuentaService activarCuentaService,
            InactivarCuentaService inactivarCuentaService,
            ObtenerCuentaService obtenerCuentaService,
            ListarCuentasPorClienteService listarPorClienteService
    ) {
        this.crearCuentaService = crearCuentaService;
        this.cancelarCuentaService = cancelarCuentaService;
        this.activarCuentaService = activarCuentaService;
        this.inactivarCuentaService = inactivarCuentaService;
        this.obtenerCuentaService = obtenerCuentaService;
        this.listarPorClienteService = listarPorClienteService;
    }


    @PostMapping
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


    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<CuentaResponse> obtener(
            @PathVariable String numeroCuenta
    ) {

        Cuenta cuenta = obtenerCuentaService.ejecutar(numeroCuenta);

        return ResponseEntity.ok(
                CuentaMapper.toResponse(cuenta)
        );
    }


    @GetMapping("/cliente/{clienteId}")
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
    public ResponseEntity<Void> cancelar(
            @PathVariable String numeroCuenta
    ) {

        cancelarCuentaService.ejecutar(numeroCuenta);

        return ResponseEntity.noContent().build();
    }


    @PatchMapping("/{numeroCuenta}/activar")
    public ResponseEntity<Void> activar(
            @PathVariable String numeroCuenta
    ) {

        activarCuentaService.ejecutar(numeroCuenta);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{numeroCuenta}/inactivar")
    public ResponseEntity<Void> inactivar(
            @PathVariable String numeroCuenta
    ) {

        inactivarCuentaService.ejecutar(numeroCuenta);

        return ResponseEntity.noContent().build();
    }
}
