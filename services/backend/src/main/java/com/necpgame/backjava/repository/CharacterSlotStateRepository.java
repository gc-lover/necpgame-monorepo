package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.CharacterSlotStateEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterSlotStateRepository extends JpaRepository<CharacterSlotStateEntity, UUID> {

    Optional<CharacterSlotStateEntity> findByAccountId(UUID accountId);
}
