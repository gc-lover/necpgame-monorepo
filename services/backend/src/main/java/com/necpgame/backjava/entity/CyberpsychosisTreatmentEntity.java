package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * JPA Entity РґР»СЏ РјРµС‚РѕРґРѕРІ Р»РµС‡РµРЅРёСЏ РєРёР±РµСЂРїСЃРёС…РѕР·Р° (СЃРїСЂР°РІРѕС‡РЅРёРє).
 * 
 * РЎРІСЏР·Р°РЅРЅР°СЏ С‚Р°Р±Р»РёС†Р°: cyberpsychosis_treatments
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/combat/cyberpsychosis.yaml
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "cyberpsychosis_treatments",
    indexes = {
        @Index(name = "idx_cyberpsychosis_treatments_type", columnList = "type"),
        @Index(name = "idx_cyberpsychosis_treatments_required_stage", columnList = "required_stage")
    }
)
public class CyberpsychosisTreatmentEntity {
    
    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;
    
    @Column(name = "name", nullable = false, length = 200)
    private String name;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    /**
     * РўРёРї Р»РµС‡РµРЅРёСЏ: therapy, medication, implant_removal, detox, social_support
     */
    @Column(name = "type", nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private TreatmentType type;
    
    @Column(name = "humanity_restore", nullable = false)
    private Float humanityRestore = 0.0f;
    
    @Column(name = "cost_credits", nullable = false)
    private Integer costCredits = 0;
    
    @Column(name = "duration_hours", nullable = false)
    private Integer durationHours = 1;
    
    /**
     * РњРёРЅРёРјР°Р»СЊРЅР°СЏ СЃС‚Р°РґРёСЏ РґР»СЏ РїСЂРёРјРµРЅРµРЅРёСЏ: early, middle, late, cyberpsychosis
     */
    @Column(name = "required_stage", length = 50)
    @Enumerated(EnumType.STRING)
    private Stage requiredStage;
    
    @Column(name = "success_rate", nullable = false)
    private Integer successRate = 100;
    
    @Column(name = "side_effects", columnDefinition = "JSONB")
    private String sideEffects;
    
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    public enum TreatmentType {
        therapy, medication, implant_removal, detox, social_support
    }
    
    public enum Stage {
        early, middle, late, cyberpsychosis
    }
}

