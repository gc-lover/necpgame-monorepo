package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterStatsSnapshotEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий снепшотов характеристик персонажей.
 */
@Repository
public interface CharacterStatsSnapshotRepository extends JpaRepository<CharacterStatsSnapshotEntity, UUID> {

    Optional<CharacterStatsSnapshotEntity> findByCharacterId(UUID characterId);

    void deleteByCharacterId(UUID characterId);
}

