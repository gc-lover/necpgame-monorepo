package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.PlayerBankSlotEntity;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerBankSlotRepository extends JpaRepository<PlayerBankSlotEntity, UUID> {

    List<PlayerBankSlotEntity> findByPlayerIdOrderBySlotIndex(UUID playerId);

    Optional<PlayerBankSlotEntity> findByPlayerIdAndSlotIndex(UUID playerId, Integer slotIndex);

    Optional<PlayerBankSlotEntity> findFirstByPlayerIdAndItemId(UUID playerId, String itemId);

    Optional<PlayerBankSlotEntity> findFirstByPlayerIdAndItemIdIsNullOrderBySlotIndex(UUID playerId);

    long countByPlayerIdAndItemIdIsNotNull(UUID playerId);
}


