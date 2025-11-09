package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity РґР»СЏ РёРјРїР»Р°РЅС‚РѕРІ (СЃРїСЂР°РІРѕС‡РЅРёРє).
 * 
 * РЎРІСЏР·Р°РЅРЅР°СЏ С‚Р°Р±Р»РёС†Р°: implants
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/combat/implants-limits.yaml
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "implants",
    indexes = {
        @Index(name = "idx_implants_type", columnList = "type"),
        @Index(name = "idx_implants_slot_type", columnList = "slot_type"),
        @Index(name = "idx_implants_rarity", columnList = "rarity")
    }
)
public class ImplantEntity {
    
    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;
    
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * РўРёРї РёРјРїР»Р°РЅС‚Р°: weapon, armor, utility, medical, neural, cyberware
     */
    @Column(name = "type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private ImplantType type;
    
    /**
     * РўРёРї СЃР»РѕС‚Р°: neural, skeletal, optical, circulatory, dermal, internal
     */
    @Column(name = "slot_type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private SlotType slotType;
    
    @Column(name = "energy_cost", nullable = false)
    private Float energyCost = 0.0f;
    
    @Column(name = "humanity_cost", nullable = false)
    private Integer humanityCost = 0;
    
    /**
     * Р РµРґРєРѕСЃС‚СЊ: common, uncommon, rare, epic, legendary
     */
    @Column(name = "rarity", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private Rarity rarity;
    
    @Column(name = "min_level", nullable = false)
    private Integer minLevel = 1;
    
    @Column(name = "stat_bonuses", columnDefinition = "JSONB")
    private String statBonuses;
    
    @Column(name = "special_effects", columnDefinition = "JSONB")
    private String specialEffects;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public enum ImplantType {
        weapon, armor, utility, medical, neural, cyberware
    }
    
    public enum SlotType {
        neural, skeletal, optical, circulatory, dermal, internal
    }
    
    public enum Rarity {
        common, uncommon, rare, epic, legendary
    }
}

