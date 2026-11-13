package com.empresa.franquicias.infrastructure.repository;

import com.empresa.franquicias.infrastructure.entity.SucursalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SucursalJpaRepository extends JpaRepository<SucursalEntity, Long> {
}
