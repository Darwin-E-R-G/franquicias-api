package com.empresa.franquicias.infrastructure.rest.controller;

import com.empresa.franquicias.domain.model.Sucursal;
import com.empresa.franquicias.domain.service.SucursalService;
import com.empresa.franquicias.infrastructure.rest.request.SucursalRequest;
import com.empresa.franquicias.infrastructure.rest.response.SucursalResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sucursales")
@RequiredArgsConstructor
@Tag(name = "Sucursales",
        description = "Operaciones de sucursales")
public class SucursalController {
    private final SucursalService sucursalService;

    @PostMapping("/save")
    public ResponseEntity<SucursalResponse> crearSucursal(@Valid @RequestBody SucursalRequest request) {
        Sucursal sucursal = sucursalService.save(request.getFranquiciaId(), request.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(sucursal));
    }

    @PostMapping("/update")
    public ResponseEntity<SucursalResponse> actualizarNombre(@Valid @RequestBody SucursalRequest request) {
        if (request.getSucursalId() == null) {
            throw new IllegalArgumentException("El ID de la sucursal es obligatorio para actualizar");
        }
        return ResponseEntity.ok(toResponse(sucursalService.updateNombre(
                request.getFranquiciaId(), request.getSucursalId(), request.getNombre())));
    }

    private SucursalResponse toResponse(Sucursal sucursal) {
        return SucursalResponse.builder()
                .id(sucursal.getId())
                .nombre(sucursal.getNombre())
                .franquiciaId(sucursal.getFranquiciaId())
                .build();
    }
}
