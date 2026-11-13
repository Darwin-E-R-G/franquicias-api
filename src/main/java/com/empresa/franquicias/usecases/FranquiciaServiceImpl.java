package com.empresa.franquicias.usecases;

import com.empresa.franquicias.domain.exception.ResourceNotFoundException;
import com.empresa.franquicias.domain.model.Franquicia;
import com.empresa.franquicias.domain.repository.FranquiciaRepository;
import com.empresa.franquicias.domain.service.FranquiciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FranquiciaServiceImpl implements FranquiciaService {

    private final FranquiciaRepository franquiciaRepository;

    @Override
    @Transactional
    public Franquicia save(Franquicia franquicia) {
        return franquiciaRepository.save(franquicia);
    }

    @Override
    @Transactional
    public Franquicia update(Franquicia franquicia) {
        if (franquicia.getId() == null) {
            throw new IllegalArgumentException("El ID de la franquicia es obligatorio para actualizar.");
        }
        Franquicia franquiciaExistente = franquiciaRepository.findById(franquicia.getId());
        franquiciaExistente.setNombre(franquicia.getNombre());
        return franquiciaRepository.update(franquiciaExistente);
    }

    @Override
    @Transactional(readOnly = true)
    public Franquicia findById(Long id) {
        return franquiciaRepository.findById(id);
    }
}