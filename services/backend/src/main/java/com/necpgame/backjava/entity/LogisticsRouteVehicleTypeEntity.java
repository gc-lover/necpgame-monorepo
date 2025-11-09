package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.LogisticsVehicleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "logistics_route_vehicle_types")
public class LogisticsRouteVehicleTypeEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private LogisticsRouteEntity route;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, length = 32)
    private LogisticsVehicleType vehicleType;
}

