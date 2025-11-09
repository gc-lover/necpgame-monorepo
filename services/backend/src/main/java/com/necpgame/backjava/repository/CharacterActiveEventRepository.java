package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterActiveEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CharacterActiveEventRepository extends JpaRepository<CharacterActiveEventEntity, UUID> {

    List<CharacterActiveEventEntity> findByCharacterIdAndStatusOrderByTriggeredAtDesc(UUID characterId, CharacterActiveEventEntity.EventStatus status);

    Optional<CharacterActiveEventEntity> findByIdAndCharacterId(UUID id, UUID characterId);

    boolean existsByCharacterIdAndEventIdAndStatus(UUID characterId, String eventId, CharacterActiveEventEntity.EventStatus status);
}

