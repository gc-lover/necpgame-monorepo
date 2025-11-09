package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterEventHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CharacterEventHistoryRepository extends JpaRepository<CharacterEventHistoryEntity, UUID> {

    Page<CharacterEventHistoryEntity> findByCharacterIdOrderByResolvedAtDesc(UUID characterId, Pageable pageable);

    Page<CharacterEventHistoryEntity> findByCharacterIdAndPeriodIgnoreCaseOrderByResolvedAtDesc(UUID characterId, String period, Pageable pageable);
}

