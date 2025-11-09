package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LogisticsCargoItemEntity;
import com.necpgame.backjava.entity.LogisticsShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LogisticsCargoItemRepository extends JpaRepository<LogisticsCargoItemEntity, UUID> {

    List<LogisticsCargoItemEntity> findByShipment(LogisticsShipmentEntity shipment);
}
