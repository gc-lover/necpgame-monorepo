package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.PlayerEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Репозиторий профилей игроков.
 */
@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, UUID> {

    Optional<PlayerEntity> findByAccountId(UUID accountId);

    boolean existsByAccountId(UUID accountId);

    @Query("SELECT p FROM PlayerEntity p LEFT JOIN FETCH p.slots WHERE p.account.id = :accountId")
    Optional<PlayerEntity> findWithSlotsByAccountId(UUID accountId);
}

