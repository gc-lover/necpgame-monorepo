package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LogisticsRouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LogisticsRouteRepository extends JpaRepository<LogisticsRouteEntity, UUID> {

    List<LogisticsRouteEntity> findByOriginIgnoreCaseAndDestinationIgnoreCase(String origin, String destination);
}
