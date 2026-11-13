package com.empresa.franquicias.infrastructure.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SucursalRequest {
    @NotNull(message = "El ID de la franquicia es obligatorio")
    private Long franquiciaId;

    private Long sucursalId;

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String nombre;
}
