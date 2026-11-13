package com.empresa.franquicias.domain.service;
import com.empresa.franquicias.domain.model.Producto;
import com.empresa.franquicias.infrastructure.rest.response.ProductoMaxStockResponse;
import java.util.List;
public interface ProductoService {
    Producto save(Long sucursalId, String nombre, Integer stock);
    void delete(Long sucursalId, Long productoId);
    Producto updateStock(Long sucursalId, Long productoId, Integer stock);
    Producto updateNombre(Long sucursalId, Long productoId, String nombre);
    List<ProductoMaxStockResponse> findMaxStockByFranquicia(Long franquiciaId);
}
