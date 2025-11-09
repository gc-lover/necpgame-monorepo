package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity РґР»СЏ Р°РєС‚РёРІРЅС‹С… СЃРёРјРїС‚РѕРјРѕРІ РєРёР±РµСЂРїСЃРёС…РѕР·Р° РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РЎРІСЏР·Р°РЅРЅР°СЏ С‚Р°Р±Р»РёС†Р°: character_active_symptoms
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/combat/cyberpsychosis.yaml
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "character_active_symptoms",
    indexes = {
        @Index(name = "idx_character_active_symptoms_character", columnList = "character_id"),
        @Index(name = "idx_character_active_symptoms_symptom", columnList = "symptom_id"),
        @Index(name = "idx_character_active_symptoms_active", columnList = "is_active")
    },
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_character_symptom", columnNames = {"character_id", "symptom_id"})
    }
)
public class CharacterActiveSymptomEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "character_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_character_active_symptoms_character"))
    private CharacterEntity character;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "symptom_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_character_active_symptoms_symptom"))
    private CyberpsychosisSymptomEntity symptom;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "triggered_count", nullable = false)
    private Integer triggeredCount = 0;
    
    @CreationTimestamp
    @Column(name = "triggered_at", nullable = false, updatable = false)
    private LocalDateTime triggeredAt;
    
    @Column(name = "suppressed_until")
    private LocalDateTime suppressedUntil;
}

