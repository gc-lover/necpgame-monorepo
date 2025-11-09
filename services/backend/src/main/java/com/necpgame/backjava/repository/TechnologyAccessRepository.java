package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.TechnologyAccessEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TechnologyAccessRepository extends JpaRepository<TechnologyAccessEntity, UUID> {

    List<TechnologyAccessEntity> findByAvailableTrue();

    List<TechnologyAccessEntity> findByAvailableTrueAndRequiredEra(String requiredEra);
}

