package com.empresa.franquicias.infrastructure.repository;
import com.empresa.franquicias.domain.exception.ResourceNotFoundException;
import com.empresa.franquicias.domain.model.Sucursal;
import com.empresa.franquicias.domain.repository.SucursalRepository;
import com.empresa.franquicias.infrastructure.entity.FranquiciaEntity;
import com.empresa.franquicias.infrastructure.entity.SucursalEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
@Repository
@RequiredArgsConstructor
public class SucursalRepositoryImpl implements SucursalRepository {
    private final SucursalJpaRepository sucursalJpaRepository;
    private final FranquiciaJpaRepository franquiciaJpaRepository;
    @Override
    public Sucursal save(Sucursal model) {
        FranquiciaEntity franquicia = franquiciaJpaRepository.findById(model.getFranquiciaId())
            .orElseThrow(() -> new ResourceNotFoundException("Franquicia no encontrada con ID: " + model.getFranquiciaId()));
        SucursalEntity entity = new SucursalEntity();
        entity.setNombre(model.getNombre());
        entity.setFranquicia(franquicia);
        return toModel(sucursalJpaRepository.save(entity));
    }
    @Override
    public Sucursal findById(Long id) { return toModel(getEntity(id)); }
    @Override
    public Sucursal update(Sucursal model) {
        SucursalEntity entity = getEntity(model.getId());
        entity.setNombre(model.getNombre());
        return toModel(sucursalJpaRepository.save(entity));
    }
    private SucursalEntity getEntity(Long id) {
        return sucursalJpaRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Sucursal no encontrada con ID: " + id));
    }
    private Sucursal toModel(SucursalEntity entity) {
        return Sucursal.builder().id(entity.getId()).nombre(entity.getNombre())
            .franquiciaId(entity.getFranquicia().getId()).build();
    }
}
