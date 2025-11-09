package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LogisticsIncidentCargoLossEntity;
import com.necpgame.backjava.entity.LogisticsIncidentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LogisticsIncidentCargoLossRepository extends JpaRepository<LogisticsIncidentCargoLossEntity, UUID> {

    List<LogisticsIncidentCargoLossEntity> findByIncident(LogisticsIncidentEntity incident);
}
