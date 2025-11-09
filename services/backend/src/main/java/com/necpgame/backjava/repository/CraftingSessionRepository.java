package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CraftingSessionEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CraftingSessionRepository extends JpaRepository<CraftingSessionEntity, UUID> {
}

