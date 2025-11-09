package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LogisticsConvoyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LogisticsConvoyRepository extends JpaRepository<LogisticsConvoyEntity, UUID> {
}
