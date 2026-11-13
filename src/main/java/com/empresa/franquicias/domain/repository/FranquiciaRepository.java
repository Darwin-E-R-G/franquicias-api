package com.empresa.franquicias.domain.repository;

import com.empresa.franquicias.domain.model.Franquicia;

public interface FranquiciaRepository {

    Franquicia save(Franquicia franquicia);

    Franquicia update(Franquicia franquicia);

    Franquicia findById(Long id);
}
