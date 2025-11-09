package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * GameLocationEntity - РёРіСЂРѕРІР°СЏ Р»РѕРєР°С†РёСЏ (СѓСЂРѕРІРµРЅСЊ, СЂР°Р№РѕРЅ, РјРµСЃС‚Рѕ).
 * 
 * РЎРїСЂР°РІРѕС‡РЅРёРє РІСЃРµС… Р»РѕРєР°С†РёР№ РІ РёРіСЂРµ СЃ РїРѕР»РЅС‹Рј РѕРїРёСЃР°РЅРёРµРј.
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/locations/locations.yaml (LocationDetails schema)
 */
@Entity
@Table(name = "game_locations", indexes = {
    @Index(name = "idx_game_locations_type", columnList = "location_type"),
    @Index(name = "idx_game_locations_danger", columnList = "danger_level")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameLocationEntity {

    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "location_type", nullable = false, length = 50)
    private String locationType; // district, building, street, underground

    @Column(name = "danger_level", nullable = false)
    private Integer dangerLevel = 1; // 1-10

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "accessible", nullable = false)
    private Boolean accessible = true;

    @Column(name = "requirements", length = 1000)
    private String requirements; // JSON: { "level": 5, "quest": "main_01" }

    @Column(name = "connected_locations", columnDefinition = "TEXT")
    private String connectedLocations; // JSON array: ["location_2", "location_3"]

    @Column(name = "available_actions", columnDefinition = "TEXT")
    private String availableActions; // JSON array: ["explore", "rest", "trade"]

    @Column(name = "npcs", columnDefinition = "TEXT")
    private String npcs; // JSON array: ["npc_1", "npc_2"]

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

