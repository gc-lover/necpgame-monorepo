package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LogisticsShipmentEntity;
import com.necpgame.backjava.entity.enums.LogisticsShipmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LogisticsShipmentRepository extends JpaRepository<LogisticsShipmentEntity, UUID> {

    List<LogisticsShipmentEntity> findByCharacterId(UUID characterId);

    List<LogisticsShipmentEntity> findByCharacterIdAndStatus(UUID characterId, LogisticsShipmentStatus status);

    Optional<LogisticsShipmentEntity> findFirstByIdAndCharacterId(UUID id, UUID characterId);
}
