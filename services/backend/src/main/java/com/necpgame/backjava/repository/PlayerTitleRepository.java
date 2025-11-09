package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.PlayerTitleEntity;
import com.necpgame.backjava.entity.PlayerTitleId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerTitleRepository extends JpaRepository<PlayerTitleEntity, PlayerTitleId> {

    List<PlayerTitleEntity> findByIdPlayerId(UUID playerId);

    Optional<PlayerTitleEntity> findByIdPlayerIdAndActiveTrue(UUID playerId);

    Optional<PlayerTitleEntity> findByIdPlayerIdAndIdTitleId(UUID playerId, UUID titleId);
}

