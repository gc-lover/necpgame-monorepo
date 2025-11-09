package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LoreUniverseEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoreUniverseRepository extends JpaRepository<LoreUniverseEntity, UUID> {

    Optional<LoreUniverseEntity> findTopByOrderByCreatedAtDesc();
}


