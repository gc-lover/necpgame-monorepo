package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.LogisticsRouteRiskLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "logistics_routes", indexes = {
        @Index(name = "idx_logistics_routes_origin_destination", columnList = "origin,destination")
})
public class LogisticsRouteEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "origin", nullable = false)
    private String origin;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "distance_km")
    private Double distanceKm;

    @Column(name = "estimated_time_hours")
    private Double estimatedTimeHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", length = 16)
    private LogisticsRouteRiskLevel riskLevel;

    @Column(name = "cost_multiplier", precision = 8, scale = 2)
    private BigDecimal costMultiplier;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY)
    private List<LogisticsRouteVehicleTypeEntity> vehicleTypes = new ArrayList<>();

    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY)
    private List<LogisticsRouteRiskEntity> risks = new ArrayList<>();

    @OneToMany(mappedBy = "route", fetch = FetchType.LAZY)
    private List<LogisticsRouteWaypointEntity> waypoints = new ArrayList<>();
}

