package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.PlayerAchievementEntity;
import com.necpgame.backjava.entity.PlayerAchievementId;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerAchievementRepository extends JpaRepository<PlayerAchievementEntity, PlayerAchievementId>, JpaSpecificationExecutor<PlayerAchievementEntity> {

    Page<PlayerAchievementEntity> findByIdPlayerId(UUID playerId, Pageable pageable);

    Page<PlayerAchievementEntity> findByIdPlayerIdAndStatus(UUID playerId, String status, Pageable pageable);

    Optional<PlayerAchievementEntity> findByIdPlayerIdAndIdAchievementId(UUID playerId, UUID achievementId);
}

