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
 * JPA Entity РґР»СЏ С‡РµР»РѕРІРµС‡РЅРѕСЃС‚Рё РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РЎРІСЏР·Р°РЅРЅР°СЏ С‚Р°Р±Р»РёС†Р°: character_humanity
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/combat/cyberpsychosis.yaml
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "character_humanity",
    indexes = {
        @Index(name = "idx_character_humanity_character", columnList = "character_id", unique = true),
        @Index(name = "idx_character_humanity_stage", columnList = "stage")
    }
)
public class CharacterHumanityEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "character_id", nullable = false, unique = true,
                foreignKey = @ForeignKey(name = "fk_character_humanity_character"))
    private CharacterEntity character;
    
    @Column(name = "current_humanity", nullable = false)
    private Float currentHumanity = 100.0f;
    
    @Column(name = "max_humanity", nullable = false)
    private Float maxHumanity = 100.0f;
    
    @Column(name = "loss_percentage", nullable = false)
    private Float lossPercentage = 0.0f;
    
    /**
     * РўРµРєСѓС‰Р°СЏ СЃС‚Р°РґРёСЏ: early, middle, late, cyberpsychosis
     */
    @Column(name = "stage", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private CyberpsychosisStage stage = CyberpsychosisStage.early;
    
    @Column(name = "total_humanity_lost", nullable = false)
    private Float totalHumanityLost = 0.0f;
    
    @Column(name = "last_loss_at")
    private LocalDateTime lastLossAt;
    
    @Column(name = "adaptation_level", nullable = false)
    private Integer adaptationLevel = 0;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public enum CyberpsychosisStage {
        early, middle, late, cyberpsychosis
    }
}

