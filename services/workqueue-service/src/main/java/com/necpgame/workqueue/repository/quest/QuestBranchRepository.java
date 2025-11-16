package com.necpgame.workqueue.repository.quest;

import com.necpgame.workqueue.domain.quest.QuestBranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface QuestBranchRepository extends JpaRepository<QuestBranchEntity, UUID> {
    List<QuestBranchEntity> findByQuestEntity_Id(UUID questId);

    @Modifying
    @Query("delete from QuestBranchEntity b where b.questEntity.id = :questId")
    void deleteByQuestId(@org.springframework.data.repository.query.Param("questId") UUID questId);
}


