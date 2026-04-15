package com.trinity.banco.rest.controller;

import com.trinity.banco.application.service.transaccion.ConsignarService;
import com.trinity.banco.application.service.transaccion.ListarTransaccionesPorCuentaService;
import com.trinity.banco.application.service.transaccion.RetirarService;
import com.trinity.banco.application.service.transaccion.TransferirService;
import com.trinity.banco.domain.model.Transaccion;
import com.trinity.banco.rest.dto.request.MovimientoRequest;
import com.trinity.banco.rest.dto.request.TransferenciaRequest;
import com.trinity.banco.rest.dto.responses.TransaccionResponse;
import com.trinity.banco.rest.mapper.TransaccionMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transacciones")
public class TransaccionController {
    private final ConsignarService consignarService;
    private final RetirarService retirarService;
    private final TransferirService transferirService;
    private final ListarTransaccionesPorCuentaService listarService;

    public TransaccionController(
            ConsignarService consignarService,
            RetirarService retirarService,
            TransferirService transferirService,
            ListarTransaccionesPorCuentaService listarService
    ) {
        this.consignarService = consignarService;
        this.retirarService = retirarService;
        this.transferirService = transferirService;
        this.listarService = listarService;
    }


    @PostMapping("/consignar")
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
