package com.empresa.franquicias.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "franquicia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FranquiciaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Builder.Default
    @OneToMany(mappedBy = "franquicia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SucursalEntity> sucursales = new ArrayList<>();
}
