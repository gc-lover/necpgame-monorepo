package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LogisticsEscortRequestEntity;
import com.necpgame.backjava.entity.LogisticsShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LogisticsEscortRequestRepository extends JpaRepository<LogisticsEscortRequestEntity, UUID> {

    List<LogisticsEscortRequestEntity> findByShipment(LogisticsShipmentEntity shipment);
}
