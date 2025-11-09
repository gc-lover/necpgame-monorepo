package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * CharacterAbilityCooldownEntity - кулдауны способностей персонажа.
 * 
 * Отслеживает когда способность будет готова к повторному использованию.
 * Источник: API-SWAGGER/api/v1/gameplay/combat/abilities.yaml (AbilityCooldown schema)
 */
@Entity
@Table(name = "character_ability_cooldowns", indexes = {
    @Index(name = "idx_character_ability_cooldowns_character", columnList = "character_id"),
    @Index(name = "idx_character_ability_cooldowns_ready", columnList = "ready_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterAbilityCooldownEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Column(name = "ability_id", nullable = false, length = 100)
    private String abilityId;

    @Column(name = "used_at", nullable = false)
    private LocalDateTime usedAt;

    @Column(name = "ready_at", nullable = false)
    private LocalDateTime readyAt;

    @Column(name = "remaining_charges")
    private Integer remainingCharges;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CharacterEntity character;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ability_id", referencedColumnName = "id", insertable = false, updatable = false)
    private AbilityEntity ability;
}

