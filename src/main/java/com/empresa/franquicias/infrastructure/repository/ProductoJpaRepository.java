package com.empresa.franquicias.infrastructure.repository;

import com.empresa.franquicias.infrastructure.entity.ProductoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoJpaRepository extends JpaRepository<ProductoEntity, Long> {

    @Query("SELECT p FROM ProductoEntity p JOIN FETCH p.sucursal s " +
           "WHERE s.franquicia.id = :franquiciaId " +
           "AND p.stock = (SELECT MAX(p2.stock) FROM ProductoEntity p2 WHERE p2.sucursal.id = s.id)")
    List<ProductoEntity> findMaxStockProductsByFranquiciaId(@Param("franquiciaId") Long franquiciaId);
}
