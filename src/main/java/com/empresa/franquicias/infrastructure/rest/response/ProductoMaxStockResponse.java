package com.empresa.franquicias.infrastructure.rest.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductoMaxStockResponse {
    private Long sucursalId;
    private String sucursalNombre;
    private Long productoId;
    private String productoNombre;
    private Integer stock;
}
