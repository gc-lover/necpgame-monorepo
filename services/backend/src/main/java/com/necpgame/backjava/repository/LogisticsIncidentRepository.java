package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LogisticsIncidentEntity;
import com.necpgame.backjava.entity.LogisticsShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LogisticsIncidentRepository extends JpaRepository<LogisticsIncidentEntity, UUID> {

    List<LogisticsIncidentEntity> findByShipment(LogisticsShipmentEntity shipment);
}
