package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.WorldEventCharacterEffectEntity;
import com.necpgame.backjava.entity.WorldEventCharacterEffectId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface WorldEventCharacterEffectRepository extends JpaRepository<WorldEventCharacterEffectEntity, WorldEventCharacterEffectId> {

    List<WorldEventCharacterEffectEntity> findByIdCharacterId(UUID characterId);
}

