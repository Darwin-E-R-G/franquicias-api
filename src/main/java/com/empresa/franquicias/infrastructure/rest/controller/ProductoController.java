package com.empresa.franquicias.infrastructure.rest.controller;

import com.empresa.franquicias.domain.model.Producto;
import com.empresa.franquicias.domain.service.ProductoService;
import com.empresa.franquicias.infrastructure.rest.request.ProductoIdentificacionRequest;
import com.empresa.franquicias.infrastructure.rest.request.ProductoNombreRequest;
import com.empresa.franquicias.infrastructure.rest.request.ProductoRequest;
import com.empresa.franquicias.infrastructure.rest.request.StockRequest;
import com.empresa.franquicias.infrastructure.rest.response.ProductoResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
@Tag(name = "Productos",
        description = "Operaciones de productos")
public class ProductoController {
    private final ProductoService productoService;

    @PostMapping("/save")
    public ResponseEntity<ProductoResponse> crearProducto(@Valid @RequestBody ProductoRequest request) {
        Producto producto = productoService.save(request.getSucursalId(), request.getNombre(), request.getStock());
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(producto));
    }

    // POST porque DELETE con body no tiene soporte uniforme en todos los clientes HTTP.
    @PostMapping("/delete")
    public ResponseEntity<Void> eliminarProducto(@Valid @RequestBody ProductoIdentificacionRequest request) {
        productoService.delete(request.getSucursalId(), request.getProductoId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/update-stock")
    public ResponseEntity<ProductoResponse> actualizarStock(@Valid @RequestBody StockRequest request) {
        return ResponseEntity.ok(toResponse(productoService.updateStock(
                request.getSucursalId(), request.getProductoId(), request.getStock())));
    }

    @PostMapping("/update-nombre")
    public ResponseEntity<ProductoResponse> actualizarNombre(@Valid @RequestBody ProductoNombreRequest request) {
        return ResponseEntity.ok(toResponse(productoService.updateNombre(
                request.getSucursalId(), request.getProductoId(), request.getNombre())));
    }

    private ProductoResponse toResponse(Producto producto) {
        return ProductoResponse.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .stock(producto.getStock())
                .sucursalId(producto.getSucursalId())
                .build();
    }
}
