package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.FactionQuestEndingEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactionQuestEndingRepository extends JpaRepository<FactionQuestEndingEntity, String> {

    List<FactionQuestEndingEntity> findByQuestIdOrderByEndingId(String questId);
}


