package com.empresa.franquicias.usecases;
import com.empresa.franquicias.domain.exception.ResourceNotFoundException;
import com.empresa.franquicias.domain.model.Sucursal;
import com.empresa.franquicias.domain.repository.FranquiciaRepository;
import com.empresa.franquicias.domain.repository.SucursalRepository;
import com.empresa.franquicias.domain.service.SucursalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class SucursalServiceImpl implements SucursalService {
    private final SucursalRepository sucursalRepository;
    private final FranquiciaRepository franquiciaRepository;
    @Override
    @Transactional
    public Sucursal save(Long franquiciaId, String nombre) {
        franquiciaRepository.findById(franquiciaId);
        return sucursalRepository.save(Sucursal.builder().franquiciaId(franquiciaId).nombre(nombre).build());
    }
    @Override
    @Transactional
    public Sucursal updateNombre(Long franquiciaId, Long sucursalId, String nombre) {
        Sucursal sucursal = sucursalRepository.findById(sucursalId);
        if (!franquiciaId.equals(sucursal.getFranquiciaId())) {
            throw new ResourceNotFoundException("Sucursal " + sucursalId + " no pertenece a la franquicia " + franquiciaId);
        }
        sucursal.setNombre(nombre);
        return sucursalRepository.update(sucursal);
    }
}
