// Producto.java
package com.empresa.franquicias.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {
    private Long id;
    private String nombre;
    private Integer stock;
    private Long sucursalId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}