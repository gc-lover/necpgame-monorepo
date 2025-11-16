package com.necpgame.workqueue.repository.quest;

import com.necpgame.workqueue.domain.quest.QuestDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuestDataRepository extends JpaRepository<QuestDataEntity, UUID> {
}


