package com.empresa.franquicias.infrastructure.repository;

import com.empresa.franquicias.infrastructure.entity.FranquiciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranquiciaJpaRepository extends JpaRepository<FranquiciaEntity, Long> {
}
