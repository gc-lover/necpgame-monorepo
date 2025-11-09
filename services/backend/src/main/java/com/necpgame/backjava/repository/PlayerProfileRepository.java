package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.PlayerProfileEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerProfileRepository extends JpaRepository<PlayerProfileEntity, UUID> {

    Optional<PlayerProfileEntity> findByAccountId(UUID accountId);
}
