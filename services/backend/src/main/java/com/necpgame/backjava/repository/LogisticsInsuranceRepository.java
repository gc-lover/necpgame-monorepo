package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LogisticsInsuranceEntity;
import com.necpgame.backjava.entity.LogisticsShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LogisticsInsuranceRepository extends JpaRepository<LogisticsInsuranceEntity, UUID> {

    Optional<LogisticsInsuranceEntity> findByShipment(LogisticsShipmentEntity shipment);
}
