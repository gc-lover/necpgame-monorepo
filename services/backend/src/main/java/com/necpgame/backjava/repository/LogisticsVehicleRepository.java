package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LogisticsVehicleEntity;
import com.necpgame.backjava.entity.enums.LogisticsVehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogisticsVehicleRepository extends JpaRepository<LogisticsVehicleEntity, LogisticsVehicleType> {
}
