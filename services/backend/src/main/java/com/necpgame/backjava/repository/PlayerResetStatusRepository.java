package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.PlayerResetStatusEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerResetStatusRepository extends JpaRepository<PlayerResetStatusEntity, UUID> {

    Optional<PlayerResetStatusEntity> findByPlayerId(UUID playerId);
}

