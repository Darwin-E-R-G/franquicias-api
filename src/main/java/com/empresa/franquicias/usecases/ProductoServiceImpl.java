package com.empresa.franquicias.usecases;
import com.empresa.franquicias.domain.exception.ResourceNotFoundException;
import com.empresa.franquicias.domain.model.Producto;
import com.empresa.franquicias.domain.repository.FranquiciaRepository;
import com.empresa.franquicias.domain.repository.ProductoRepository;
import com.empresa.franquicias.domain.repository.SucursalRepository;
import com.empresa.franquicias.domain.service.ProductoService;
import com.empresa.franquicias.infrastructure.rest.response.ProductoMaxStockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository productoRepository;
    private final SucursalRepository sucursalRepository;
    private final FranquiciaRepository franquiciaRepository;
    @Override
    @Transactional
    public Producto save(Long sucursalId, String nombre, Integer stock) {
        sucursalRepository.findById(sucursalId);
        return productoRepository.save(Producto.builder().sucursalId(sucursalId).nombre(nombre).stock(stock).build());
    }
    @Override
    @Transactional
    public void delete(Long sucursalId, Long productoId) {
        getProductoDeSucursal(sucursalId, productoId);
        productoRepository.deleteById(productoId);
    }
    @Override
    @Transactional
    public Producto updateStock(Long sucursalId, Long productoId, Integer stock) {
        Producto producto = getProductoDeSucursal(sucursalId, productoId);
        producto.setStock(stock);
        return productoRepository.update(producto);
    }
    @Override
    @Transactional
    public Producto updateNombre(Long sucursalId, Long productoId, String nombre) {
        Producto producto = getProductoDeSucursal(sucursalId, productoId);
        producto.setNombre(nombre);
        return productoRepository.update(producto);
    }
    @Override
    @Transactional(readOnly = true)
    public List<ProductoMaxStockResponse> findMaxStockByFranquicia(Long franquiciaId) {
        franquiciaRepository.findById(franquiciaId);
        return productoRepository.findMaxStockByFranquiciaId(franquiciaId);
    }
    private Producto getProductoDeSucursal(Long sucursalId, Long productoId) {
        Producto producto = productoRepository.findById(productoId);
        if (!sucursalId.equals(producto.getSucursalId())) {
            throw new ResourceNotFoundException("Producto " + productoId + " no pertenece a la sucursal " + sucursalId);
        }
        return producto;
    }
}
