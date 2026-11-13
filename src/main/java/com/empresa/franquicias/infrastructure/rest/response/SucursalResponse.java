package com.empresa.franquicias.infrastructure.rest.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SucursalResponse {
    private Long id;
    private String nombre;
    private Long franquiciaId;
}
