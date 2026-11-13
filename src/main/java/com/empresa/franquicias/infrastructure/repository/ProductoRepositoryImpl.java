package com.empresa.franquicias.infrastructure.repository;
import com.empresa.franquicias.domain.exception.ResourceNotFoundException;
import com.empresa.franquicias.domain.model.Producto;
import com.empresa.franquicias.domain.repository.ProductoRepository;
import com.empresa.franquicias.infrastructure.entity.ProductoEntity;
import com.empresa.franquicias.infrastructure.entity.SucursalEntity;
import com.empresa.franquicias.infrastructure.rest.response.ProductoMaxStockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.stream.Collectors;
@Repository
@RequiredArgsConstructor
public class ProductoRepositoryImpl implements ProductoRepository {
    private final ProductoJpaRepository productoJpaRepository;
    private final SucursalJpaRepository sucursalJpaRepository;
    @Override
    public Producto save(Producto model) {
        SucursalEntity sucursal = sucursalJpaRepository.findById(model.getSucursalId())
            .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada con ID: " + model.getSucursalId()));
        ProductoEntity entity = new ProductoEntity();
        entity.setNombre(model.getNombre());
        entity.setStock(model.getStock());
        entity.setSucursal(sucursal);
        return toModel(productoJpaRepository.save(entity));
    }
    @Override
    public Producto findById(Long id) { return toModel(getEntity(id)); }
    @Override
    public Producto update(Producto model) {
        ProductoEntity entity = getEntity(model.getId());
        entity.setNombre(model.getNombre());
        entity.setStock(model.getStock());
        return toModel(productoJpaRepository.save(entity));
    }
    @Override
    public void deleteById(Long id) { productoJpaRepository.delete(getEntity(id)); }
    @Override
    public List<ProductoMaxStockResponse> findMaxStockByFranquiciaId(Long franquiciaId) {
        return productoJpaRepository.findMaxStockProductsByFranquiciaId(franquiciaId).stream()
            .map(p -> ProductoMaxStockResponse.builder()
                .sucursalId(p.getSucursal().getId())
                .sucursalNombre(p.getSucursal().getNombre())
                .productoId(p.getId()).productoNombre(p.getNombre()).stock(p.getStock()).build())
            .collect(Collectors.toList());
    }
    private ProductoEntity getEntity(Long id) {
        return productoJpaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + id));
    }
    private Producto toModel(ProductoEntity entity) {
        return Producto.builder().id(entity.getId()).nombre(entity.getNombre())
            .stock(entity.getStock()).sucursalId(entity.getSucursal().getId()).build();
    }
}
