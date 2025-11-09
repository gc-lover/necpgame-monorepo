package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity РґР»СЏ С‚Р°Р±Р»РёС†С‹ cities - РіРѕСЂРѕРґР° (СЃРїСЂР°РІРѕС‡РЅРёРє)
 * РЎРѕРѕС‚РІРµС‚СЃС‚РІСѓРµС‚ City DTO РёР· OpenAPI СЃРїРµС†РёС„РёРєР°С†РёРё
 */
@Data
@Entity
@Table(name = "cities", indexes = {
    @Index(name = "idx_cities_name", columnList = "name"),
    @Index(name = "idx_cities_region", columnList = "region")
})
@NoArgsConstructor
@AllArgsConstructor
public class CityEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name; // Night City, New York
    
    @Column(name = "region", nullable = false, length = 10)
    private String region; // EU, US, ASIA
    
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    
    // Relationships - many-to-many СЃ Factions
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "city_available_factions",
        joinColumns = @JoinColumn(name = "city_id"),
        inverseJoinColumns = @JoinColumn(name = "faction_id")
    )
    private List<FactionEntity> availableFactions = new ArrayList<>();
}

