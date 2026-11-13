package com.empresa.franquicias.domain.repository;
import com.empresa.franquicias.domain.model.Producto;
import com.empresa.franquicias.infrastructure.rest.response.ProductoMaxStockResponse;
import java.util.List;
public interface ProductoRepository {
    Producto save(Producto producto);
    Producto findById(Long id);
    Producto update(Producto producto);
    void deleteById(Long id);
    List<ProductoMaxStockResponse> findMaxStockByFranquiciaId(Long franquiciaId);
}
