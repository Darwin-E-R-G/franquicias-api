package com.empresa.franquicias.domain.service;
import com.empresa.franquicias.domain.model.Sucursal;
public interface SucursalService {
    Sucursal save(Long franquiciaId, String nombre);
    Sucursal updateNombre(Long franquiciaId, Long sucursalId, String nombre);
}
