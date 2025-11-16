package com.necpgame.workqueue.repository.quest;

import com.necpgame.workqueue.domain.quest.QuestRewardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface QuestRewardRepository extends JpaRepository<QuestRewardEntity, UUID> {
    List<QuestRewardEntity> findByQuestEntity_Id(UUID questId);

    @Modifying
    @Query("delete from QuestRewardEntity r where r.questEntity.id = :questId")
    void deleteByQuestId(@org.springframework.data.repository.query.Param("questId") UUID questId);
}


