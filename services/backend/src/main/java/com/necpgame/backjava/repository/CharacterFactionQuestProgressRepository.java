package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterFactionQuestProgressEntity;
import com.necpgame.backjava.entity.CharacterFactionQuestProgressEntity.ProgressStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterFactionQuestProgressRepository extends JpaRepository<CharacterFactionQuestProgressEntity, UUID> {

    List<CharacterFactionQuestProgressEntity> findByCharacterId(UUID characterId);

    List<CharacterFactionQuestProgressEntity> findByCharacterIdAndStatus(UUID characterId, ProgressStatus status);

    Optional<CharacterFactionQuestProgressEntity> findByCharacterIdAndQuestId(UUID characterId, String questId);

    List<CharacterFactionQuestProgressEntity> findByQuestIdAndStatus(String questId, ProgressStatus status);
}


