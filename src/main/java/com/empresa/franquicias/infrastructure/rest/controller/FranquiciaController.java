package com.empresa.franquicias.infrastructure.rest.controller;

import com.empresa.franquicias.domain.model.Franquicia;
import com.empresa.franquicias.domain.service.FranquiciaService;
import com.empresa.franquicias.domain.service.ProductoService;
import com.empresa.franquicias.infrastructure.converter.MapperAll;
import com.empresa.franquicias.infrastructure.rest.request.FranquiciaIdRequest;
import com.empresa.franquicias.infrastructure.rest.request.FranquiciaRequest;
import com.empresa.franquicias.infrastructure.rest.response.FranquiciaResponse;
import com.empresa.franquicias.infrastructure.rest.response.ProductoMaxStockResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/franquicias")
@RequiredArgsConstructor
@Tag(name = "Franquicias",
        description = "Operaciones de franquicias")
public class FranquiciaController {
    private final FranquiciaService franquiciaService;
    private final MapperAll mapperAll;
    private final ProductoService productoService;

    @PostMapping("/save")
    public ResponseEntity<FranquiciaResponse> crearFranquicia(@Valid @RequestBody FranquiciaRequest request) {
        Franquicia franquiciaCreada = franquiciaService.save(mapperAll.tomodelreq(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapperAll.toResponse(franquiciaCreada));
    }

    @PostMapping("/update")
    public ResponseEntity<FranquiciaResponse> actualizarFranquicia(@Valid @RequestBody FranquiciaRequest request) {
        Franquicia franquiciaActualizada = franquiciaService.update(mapperAll.tomodelreq(request));
        return ResponseEntity.ok(mapperAll.toResponse(franquiciaActualizada));
    }

    // POST permite transportar el identificador en JSON de forma consistente.
    @GetMapping("/{franquiciaId}/productos/max-stock")
    public ResponseEntity<List<ProductoMaxStockResponse>> obtenerProductosMayorStock(@PathVariable Long franquiciaId) {
        return ResponseEntity.ok(productoService.findMaxStockByFranquicia(franquiciaId));
    }
}
