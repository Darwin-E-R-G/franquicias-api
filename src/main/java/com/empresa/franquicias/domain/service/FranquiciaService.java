package com.empresa.franquicias.domain.service;

import com.empresa.franquicias.domain.model.Franquicia;

public interface FranquiciaService {
    Franquicia save(Franquicia franquicia);

    Franquicia update(Franquicia franquicia);

    Franquicia findById(Long id);
}
