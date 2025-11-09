package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.EconomicMultiplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EconomicMultiplierRepository extends JpaRepository<EconomicMultiplierEntity, UUID> {

    Optional<EconomicMultiplierEntity> findFirstByOrderByUpdatedAtDesc();
}

