package com.empresa.franquicias.domain.repository;
import com.empresa.franquicias.domain.model.Sucursal;
public interface SucursalRepository {
    Sucursal save(Sucursal sucursal);
    Sucursal findById(Long id);
    Sucursal update(Sucursal sucursal);
}
