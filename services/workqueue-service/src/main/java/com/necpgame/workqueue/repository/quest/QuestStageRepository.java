package com.necpgame.workqueue.repository.quest;

import com.necpgame.workqueue.domain.quest.QuestStageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface QuestStageRepository extends JpaRepository<QuestStageEntity, UUID> {
    List<QuestStageEntity> findByQuestEntity_IdOrderByStageIndexAsc(UUID questId);

    @Modifying
    @Query("delete from QuestStageEntity s where s.questEntity.id = :questId")
    void deleteByQuestId(@org.springframework.data.repository.query.Param("questId") UUID questId);
}


