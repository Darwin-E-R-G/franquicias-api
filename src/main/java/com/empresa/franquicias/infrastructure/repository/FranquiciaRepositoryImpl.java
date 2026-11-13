package com.empresa.franquicias.infrastructure.repository;

import com.empresa.franquicias.domain.exception.ResourceNotFoundException;
import com.empresa.franquicias.domain.model.Franquicia;
import com.empresa.franquicias.domain.repository.FranquiciaRepository;
import com.empresa.franquicias.infrastructure.converter.MapperAll;
import com.empresa.franquicias.infrastructure.entity.FranquiciaEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FranquiciaRepositoryImpl implements FranquiciaRepository {

    @Autowired
    FranquiciaJpaRepository franquiciaJpaRepository;

    private final MapperAll mapperAll;

    public FranquiciaRepositoryImpl(MapperAll mapperAll) {
        this.mapperAll = mapperAll;
    }


    @Override
    public Franquicia save(Franquicia franquicia) {
        FranquiciaEntity franquiciaEntity = mapperAll.toEntity(franquicia);
        franquiciaEntity = franquiciaJpaRepository.save(franquiciaEntity);
        return mapperAll.toModel(franquiciaEntity);
    }

    @Override
    public Franquicia update(Franquicia franquicia) {
        FranquiciaEntity franquiciaEntity = franquiciaJpaRepository.findById(franquicia.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Franquicia no encontrada con ID: " + franquicia.getId()));
        franquiciaEntity.setNombre(franquicia.getNombre());
        return mapperAll.toModel(franquiciaJpaRepository.save(franquiciaEntity));
    }

    @Override
    public Franquicia findById(Long id) {
        FranquiciaEntity entity = franquiciaJpaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Franquicia no encontrada con ID: " + id));
        return mapperAll.toModel(entity);
    }
}
