package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.QuestSkillCheckResultEntity;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestSkillCheckResultRepository extends JpaRepository<QuestSkillCheckResultEntity, UUID> {

    List<QuestSkillCheckResultEntity> findByQuestInstanceId(UUID questInstanceId);
}



