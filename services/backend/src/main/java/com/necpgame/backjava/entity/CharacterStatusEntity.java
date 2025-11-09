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
 * CharacterStatusEntity - С‚РµРєСѓС‰РёР№ СЃС‚Р°С‚СѓСЃ РїРµСЂСЃРѕРЅР°Р¶Р° (Р·РґРѕСЂРѕРІСЊРµ, СЌРЅРµСЂРіРёСЏ, С‡РµР»РѕРІРµС‡РЅРѕСЃС‚СЊ, РѕРїС‹С‚).
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/characters/status.yaml (CharacterStatus schema)
 */
@Entity
@Table(name = "character_status", indexes = {
    @Index(name = "idx_character_status_character", columnList = "character_id", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "character_id", nullable = false, unique = true)
    private UUID characterId;

    @Column(name = "health", nullable = false)
    private Integer health = 100;

    @Column(name = "max_health", nullable = false)
    private Integer maxHealth = 100;

    @Column(name = "energy", nullable = false)
    private Integer energy = 100;

    @Column(name = "max_energy", nullable = false)
    private Integer maxEnergy = 100;

    @Column(name = "humanity", nullable = false)
    private Integer humanity = 100;

    @Column(name = "max_humanity", nullable = false)
    private Integer maxHumanity = 100;

    @Column(name = "level", nullable = false)
    private Integer level = 1;

    @Column(name = "experience", nullable = false)
    private Integer experience = 0;

    @Column(name = "next_level_experience", nullable = false)
    private Integer nextLevelExperience = 1000;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationship
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CharacterEntity character;
}

