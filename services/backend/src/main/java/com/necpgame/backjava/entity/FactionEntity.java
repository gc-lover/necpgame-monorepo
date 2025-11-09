package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entity РґР»СЏ С‚Р°Р±Р»РёС†С‹ factions - С„СЂР°РєС†РёРё (СЃРїСЂР°РІРѕС‡РЅРёРє)
 * РЎРѕРѕС‚РІРµС‚СЃС‚РІСѓРµС‚ Faction DTO РёР· OpenAPI СЃРїРµС†РёС„РёРєР°С†РёРё
 */
@Data
@Entity
@Table(name = "factions", indexes = {
    @Index(name = "idx_factions_name", columnList = "name")
})
@NoArgsConstructor
@AllArgsConstructor
public class FactionEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name; // Arasaka, Militech, Valentinos
    
    @Column(name = "type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private FactionType type;
    
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "available_for_origins", columnDefinition = "TEXT")
    private String availableForOrigins; // JSON array: ["corpo", "street_kid"]
    
    // Relationships - many-to-many СЃ Origins (РѕР±СЂР°С‚РЅР°СЏ СЃС‚РѕСЂРѕРЅР°)
    @ManyToMany(mappedBy = "availableFactions", fetch = FetchType.LAZY)
    private List<CharacterOriginEntity> origins = new ArrayList<>();
    
    // Relationships - many-to-many СЃ Cities
    @ManyToMany(mappedBy = "availableFactions", fetch = FetchType.LAZY)
    private List<CityEntity> cities = new ArrayList<>();
    
    // Enum РґР»СЏ С‚РёРїР° С„СЂР°РєС†РёРё
    public enum FactionType {
        corporation,
        gang,
        organization
    }
}

