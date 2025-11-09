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
 * CharacterAbilityLoadoutEntity - конфигурация способностей персонажа.
 * 
 * Хранит текущую конфигурацию способностей (Q/E/R слоты).
 * Источник: API-SWAGGER/api/v1/gameplay/combat/abilities.yaml (AbilityLoadout schema)
 */
@Entity
@Table(name = "character_ability_loadout", indexes = {
    @Index(name = "idx_character_ability_loadout_character", columnList = "character_id"),
    @Index(name = "idx_character_ability_loadout_slot", columnList = "character_id, slot_type", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterAbilityLoadoutEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Column(name = "slot_type", nullable = false, length = 10)
    private String slotType; // Q, E, R, C, X

    @Column(name = "ability_id", length = 100)
    private String abilityId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CharacterEntity character;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ability_id", referencedColumnName = "id", insertable = false, updatable = false)
    private AbilityEntity ability;
}

