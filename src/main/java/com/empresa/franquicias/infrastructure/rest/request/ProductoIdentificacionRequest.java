package com.empresa.franquicias.infrastructure.rest.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductoIdentificacionRequest {
    @NotNull(message = "El ID de la sucursal es obligatorio")
    private Long sucursalId;

    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;
}
