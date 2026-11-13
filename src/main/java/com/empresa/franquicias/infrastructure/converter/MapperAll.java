package com.empresa.franquicias.infrastructure.converter;

import com.empresa.franquicias.domain.model.Franquicia;
import com.empresa.franquicias.infrastructure.entity.FranquiciaEntity;
import com.empresa.franquicias.infrastructure.rest.request.FranquiciaRequest;
import com.empresa.franquicias.infrastructure.rest.response.FranquiciaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MapperAll {

    @Mapping(target = "sucursales", ignore = true)
    Franquicia toModel(FranquiciaEntity entity);

    @Mapping(target = "sucursales", ignore = true)
    FranquiciaEntity toEntity(Franquicia model);

    Franquicia tomodelreq(FranquiciaRequest request);

    FranquiciaResponse toResponse(Franquicia model);
}