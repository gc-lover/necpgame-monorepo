package com.necpgame.backjava.repository;

import com.necpgame.backjava.entity.LogisticsInsurancePlanEntity;
import com.necpgame.backjava.entity.enums.LogisticsInsurancePlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogisticsInsurancePlanRepository extends JpaRepository<LogisticsInsurancePlanEntity, LogisticsInsurancePlan> {
}
