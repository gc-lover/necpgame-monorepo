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
 * JPA Entity РґР»СЏ СЃС‚Р°С‚РёСЃС‚РёРєРё РёРјРїР»Р°РЅС‚РѕРІ Рё СЌРЅРµСЂРіРёРё РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РЎРІСЏР·Р°РЅРЅР°СЏ С‚Р°Р±Р»РёС†Р°: character_implant_stats
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/combat/implants-limits.yaml
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "character_implant_stats",
    indexes = {
        @Index(name = "idx_character_implant_stats_character", columnList = "character_id", unique = true)
    }
)
public class CharacterImplantStatsEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "character_id", nullable = false, unique = true, 
                foreignKey = @ForeignKey(name = "fk_character_implant_stats_character"))
    private CharacterEntity character;
    
    // ===== Р›РёРјРёС‚С‹ РёРјРїР»Р°РЅС‚РѕРІ =====
    
    @Column(name = "base_implant_limit", nullable = false)
    private Integer baseImplantLimit = 10;
    
    @Column(name = "implant_limit_bonus", nullable = false)
    private Integer implantLimitBonus = 0;
    
    @Column(name = "humanity_penalty", nullable = false)
    private Integer humanityPenalty = 0;
    
    @Column(name = "current_implant_limit", nullable = false)
    private Integer currentImplantLimit = 10;
    
    @Column(name = "used_implants", nullable = false)
    private Integer usedImplants = 0;
    
    // ===== Р­РЅРµСЂРіРµС‚РёС‡РµСЃРєРёР№ РїСѓР» =====
    
    @Column(name = "total_energy_pool", nullable = false)
    private Float totalEnergyPool = 100.0f;
    
    @Column(name = "used_energy", nullable = false)
    private Float usedEnergy = 0.0f;
    
    @Column(name = "available_energy", nullable = false)
    private Float availableEnergy = 100.0f;
    
    @Column(name = "energy_regen_rate", nullable = false)
    private Float energyRegenRate = 1.0f;
    
    @Column(name = "current_energy_level", nullable = false)
    private Float currentEnergyLevel = 100.0f;
    
    @Column(name = "max_energy_level")
    private Float maxEnergyLevel = 100.0f;
    
    // ===== Р’СЂРµРјРµРЅРЅС‹Рµ РјРѕРґРёС„РёРєР°С‚РѕСЂС‹ =====
    
    @Column(name = "can_exceed_limit_temporarily", nullable = false)
    private Boolean canExceedLimitTemporarily = false;
    
    @Column(name = "temporary_bonus_expires_at")
    private LocalDateTime temporaryBonusExpiresAt;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

