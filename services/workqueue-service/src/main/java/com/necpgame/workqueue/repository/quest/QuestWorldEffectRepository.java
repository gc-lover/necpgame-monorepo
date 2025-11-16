package com.necpgame.workqueue.repository.quest;

import com.necpgame.workqueue.domain.quest.QuestWorldEffectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface QuestWorldEffectRepository extends JpaRepository<QuestWorldEffectEntity, UUID> {
    List<QuestWorldEffectEntity> findByQuestEntity_Id(UUID questId);

    @Modifying
    @Query("delete from QuestWorldEffectEntity e where e.questEntity.id = :questId")
    void deleteByQuestId(@org.springframework.data.repository.query.Param("questId") UUID questId);
}


