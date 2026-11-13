package com.empresa.franquicias.infrastructure.rest.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FranquiciaIdRequest {
    @NotNull(message = "El ID de la franquicia es obligatorio")
    private Long franquiciaId;
}
