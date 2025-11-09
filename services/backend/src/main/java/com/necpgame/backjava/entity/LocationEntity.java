package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * LocationEntity - Р»РѕРєР°С†РёСЏ РІ РёРіСЂРµ.
 * 
 * РҐСЂР°РЅРёС‚ РёРЅС„РѕСЂРјР°С†РёСЋ Рѕ Р»РѕРєР°С†РёСЏС… (Downtown, Watson Рё С‚.Рґ.).
 */
@Entity
@Table(name = "locations", indexes = {
    @Index(name = "idx_locations_city", columnList = "city"),
    @Index(name = "idx_locations_district", columnList = "district"),
    @Index(name = "idx_locations_danger_level", columnList = "danger_level")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationEntity {

    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", nullable = false, length = 2000)
    private String description;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "district", length = 100)
    private String district;

    @Column(name = "danger_level", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private DangerLevel dangerLevel;

    @Column(name = "min_level")
    private Integer minLevel;

    @Column(name = "type", length = 20)
    @Enumerated(EnumType.STRING)
    private LocationType type;

    @Column(name = "connected_locations", length = 1000)
    private String connectedLocations; // JSON array of location IDs

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * РЈСЂРѕРІРµРЅСЊ РѕРїР°СЃРЅРѕСЃС‚Рё Р»РѕРєР°С†РёРё
     */
    public enum DangerLevel {
        LOW,
        MEDIUM,
        HIGH
    }

    /**
     * РўРёРї Р»РѕРєР°С†РёРё
     */
    public enum LocationType {
        CORPORATE,
        INDUSTRIAL,
        RESIDENTIAL,
        CRIMINAL
    }
}

