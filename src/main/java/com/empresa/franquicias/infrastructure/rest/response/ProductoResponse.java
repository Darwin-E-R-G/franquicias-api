package com.empresa.franquicias.infrastructure.rest.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductoResponse {
    private Long id;
    private String nombre;
    private Integer stock;
    private Long sucursalId;
}
