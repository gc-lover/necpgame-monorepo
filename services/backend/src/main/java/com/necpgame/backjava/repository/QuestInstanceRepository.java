package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.QuestInstanceEntity;
import com.necpgame.backjava.entity.QuestInstanceEntity.QuestStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestInstanceRepository extends JpaRepository<QuestInstanceEntity, UUID> {

    Optional<QuestInstanceEntity> findByIdAndCharacterId(UUID id, UUID characterId);

    List<QuestInstanceEntity> findByCharacterIdAndStatus(UUID characterId, QuestStatus status);

    boolean existsByCharacterIdAndQuestTemplateIdAndStatus(UUID characterId, String questTemplateId, QuestStatus status);
}



