// Sucursal.java
package com.empresa.franquicias.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {
    private Long id;
    private String nombre;
    private Long franquiciaId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<Producto> productos;
}