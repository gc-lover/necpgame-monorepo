package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.LogisticsInsurancePlan;
import com.necpgame.backjava.entity.enums.LogisticsShipmentPriority;
import com.necpgame.backjava.entity.enums.LogisticsShipmentStatus;
import com.necpgame.backjava.entity.enums.LogisticsVehicleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "logistics_shipments", indexes = {
        @Index(name = "idx_logistics_shipments_character", columnList = "character_id"),
        @Index(name = "idx_logistics_shipments_status", columnList = "status")
})
public class LogisticsShipmentEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private LogisticsShipmentStatus status;

    @Column(name = "origin", nullable = false)
    private String origin;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, length = 32)
    private LogisticsVehicleType vehicleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 16)
    private LogisticsShipmentPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(name = "insurance_plan", length = 16)
    private LogisticsInsurancePlan insurancePlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private LogisticsRouteEntity route;

    @Column(name = "estimated_delivery")
    private OffsetDateTime estimatedDelivery;

    @Column(name = "actual_delivery")
    private OffsetDateTime actualDelivery;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "current_location")
    private String currentLocation;

    @Column(name = "progress_percentage", precision = 5, scale = 2)
    private BigDecimal progressPercentage;

    @Column(name = "insurance_cost", precision = 12, scale = 2)
    private BigDecimal insuranceCost;

    @Column(name = "escort_requested", nullable = false)
    private boolean escortRequested;

    @OneToMany(mappedBy = "shipment", fetch = FetchType.LAZY)
    private List<LogisticsCargoItemEntity> cargoItems = new ArrayList<>();

    @OneToMany(mappedBy = "shipment", fetch = FetchType.LAZY)
    private List<LogisticsIncidentEntity> incidents = new ArrayList<>();

    @OneToMany(mappedBy = "shipment", fetch = FetchType.LAZY)
    private List<LogisticsTrackingEventEntity> trackingEvents = new ArrayList<>();
}

