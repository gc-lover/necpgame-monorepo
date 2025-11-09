package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * CraftingStationEntity - игровые станции крафта.
 */
@Entity
@Table(name = "crafting_stations", indexes = {
    @Index(name = "idx_crafting_stations_type", columnList = "station_type"),
    @Index(name = "idx_crafting_stations_available", columnList = "available")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CraftingStationEntity {

    @Id
    @Column(name = "station_id", nullable = false, length = 120)
    private String stationId;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "station_type", length = 80, nullable = false)
    private String stationType;

    @Column(name = "location_id", length = 120)
    private String locationId;

    @Column(name = "available", nullable = false)
    private Boolean available;

    @Column(name = "success_rate_bonus", precision = 6, scale = 4)
    private Double successRateBonus;

    @Column(name = "time_reduction", precision = 6, scale = 4)
    private Double timeReduction;

    @Column(name = "quality_chance_bonus", precision = 6, scale = 4)
    private Double qualityChanceBonus;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

