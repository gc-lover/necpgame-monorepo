package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LoreFactionEntity;
import com.necpgame.backjava.entity.enums.LoreFactionType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LoreFactionRepository extends JpaRepository<LoreFactionEntity, UUID>, JpaSpecificationExecutor<LoreFactionEntity> {

    Optional<LoreFactionEntity> findByExternalId(String externalId);

    List<LoreFactionEntity> findTop10ByNameContainingIgnoreCase(String name);

    List<LoreFactionEntity> findTop10ByDescriptionShortContainingIgnoreCase(String description);

    List<LoreFactionEntity> findTop10ByType(LoreFactionType type);
}
