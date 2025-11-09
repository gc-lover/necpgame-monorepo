package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LogisticsShipmentEntity;
import com.necpgame.backjava.entity.LogisticsTrackingEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LogisticsTrackingEventRepository extends JpaRepository<LogisticsTrackingEventEntity, UUID> {

    List<LogisticsTrackingEventEntity> findByShipmentOrderByOccurredAtAsc(LogisticsShipmentEntity shipment);
}
