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
 * CharacterWeaponMasteryEntity - мастерство оружия персонажа.
 * 
 * Хранит прогресс владения оружием для каждого класса оружия.
 * Источник: API-SWAGGER/api/v1/gameplay/combat/weapons.yaml (WeaponMasteryProgress schema)
 */
@Entity
@Table(name = "character_weapon_mastery", indexes = {
    @Index(name = "idx_character_weapon_mastery_character", columnList = "character_id"),
    @Index(name = "idx_character_weapon_mastery_class", columnList = "weapon_class"),
    @Index(name = "idx_character_weapon_mastery_character_class", columnList = "character_id, weapon_class", unique = true)
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterWeaponMasteryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Column(name = "weapon_class", nullable = false, length = 50)
    private String weaponClass; // pistol, assault_rifle, shotgun, etc.

    @Column(name = "mastery_rank", nullable = false)
    private Integer masteryRank = 1; // 1-5: Novice, Apprentice, Adept, Expert, Legend

    @Column(name = "experience", nullable = false)
    private Integer experience = 0;

    @Column(name = "next_rank_experience", nullable = false)
    private Integer nextRankExperience = 1000;

    @Column(name = "kills_with_class", nullable = false)
    private Integer killsWithClass = 0;

    @Column(name = "headshots_with_class", nullable = false)
    private Integer headshotsWithClass = 0;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CharacterEntity character;
}

