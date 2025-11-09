package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity РґР»СЏ С‚Р°Р±Р»РёС†С‹ character_origins - РїСЂРѕРёСЃС…РѕР¶РґРµРЅРёСЏ РїРµСЂСЃРѕРЅР°Р¶РµР№ (СЃРїСЂР°РІРѕС‡РЅРёРє)
 * РЎРѕРѕС‚РІРµС‚СЃС‚РІСѓРµС‚ CharacterOrigin DTO РёР· OpenAPI СЃРїРµС†РёС„РёРєР°С†РёРё
 */
@Data
@Entity
@Table(name = "character_origins", indexes = {
    @Index(name = "idx_character_origins_code", columnList = "origin_code", unique = true)
})
@NoArgsConstructor
@AllArgsConstructor
public class CharacterOriginEntity {
    
    @Id
    @Column(name = "origin_code", length = 50, nullable = false)
    private String originCode; // street_kid, corpo, nomad
    
    @Column(name = "name", nullable = false, length = 100)
    private String name; // РЈР»РёС‡РЅС‹Р№ Р±СЂРѕРґСЏРіР°, РљРѕСЂРїРѕСЂР°С‚, РљРѕС‡РµРІРЅРёРє
    
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "starting_skills", columnDefinition = "TEXT")
    private String startingSkills; // JSON array: ["street_combat", "survival"]
    
    @Column(name = "starting_currency", nullable = false)
    private Integer startingCurrency; // 1000
    
    @Column(name = "starting_items", columnDefinition = "TEXT")
    private String startingItems; // JSON array: ["basic_pistol", "street_clothes"]
    
    // Relationships - many-to-many СЃ Factions
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "origin_available_factions",
        joinColumns = @JoinColumn(name = "origin_code"),
        inverseJoinColumns = @JoinColumn(name = "faction_id")
    )
    private List<FactionEntity> availableFactions = new ArrayList<>();
}

