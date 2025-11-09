package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.FactionQuestBranchEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FactionQuestBranchRepository extends JpaRepository<FactionQuestBranchEntity, String> {

    List<FactionQuestBranchEntity> findByQuestIdOrderByBranchId(String questId);
}


