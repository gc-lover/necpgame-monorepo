package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.QuestDialogueStateEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestDialogueStateRepository extends JpaRepository<QuestDialogueStateEntity, UUID> {

    Optional<QuestDialogueStateEntity> findByQuestInstanceId(UUID questInstanceId);
}



