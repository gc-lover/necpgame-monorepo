package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.AchievementEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementRepository extends JpaRepository<AchievementEntity, UUID>, JpaSpecificationExecutor<AchievementEntity> {
}

