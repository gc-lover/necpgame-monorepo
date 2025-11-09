package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.FactionQuestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FactionQuestRepository extends JpaRepository<FactionQuestEntity, String>, JpaSpecificationExecutor<FactionQuestEntity> {
}


