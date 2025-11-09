package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CraftingStationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CraftingStationRepository extends JpaRepository<CraftingStationEntity, String> {
}

