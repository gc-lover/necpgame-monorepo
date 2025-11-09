package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.LogisticsVehicleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "logistics_vehicles")
public class LogisticsVehicleEntity {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, length = 32)
    private LogisticsVehicleType vehicleType;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "speed_multiplier", precision = 6, scale = 2)
    private BigDecimal speedMultiplier;

    @Column(name = "capacity_weight", precision = 12, scale = 2)
    private BigDecimal capacityWeight;

    @Column(name = "capacity_volume", precision = 12, scale = 2)
    private BigDecimal capacityVolume;

    @Column(name = "risk_modifier", precision = 6, scale = 2)
    private BigDecimal riskModifier;

    @Column(name = "cost_multiplier", precision = 6, scale = 2)
    private BigDecimal costMultiplier;
}

