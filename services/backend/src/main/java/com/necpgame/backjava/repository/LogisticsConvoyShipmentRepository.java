package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LogisticsConvoyEntity;
import com.necpgame.backjava.entity.LogisticsConvoyShipmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LogisticsConvoyShipmentRepository extends JpaRepository<LogisticsConvoyShipmentEntity, UUID> {

    List<LogisticsConvoyShipmentEntity> findByConvoy(LogisticsConvoyEntity convoy);
}
