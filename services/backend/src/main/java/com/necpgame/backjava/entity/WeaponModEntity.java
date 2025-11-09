package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * WeaponModEntity - модификация оружия (справочник).
 * 
 * Справочник всех модов для оружия (прицелы, глушители, увеличенные магазины).
 * Источник: API-SWAGGER/api/v1/gameplay/combat/weapons.yaml (WeaponMod schema)
 */
@Entity
@Table(name = "weapon_mods", indexes = {
    @Index(name = "idx_weapon_mods_type", columnList = "mod_type"),
    @Index(name = "idx_weapon_mods_slot", columnList = "slot_type"),
    @Index(name = "idx_weapon_mods_rarity", columnList = "rarity")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeaponModEntity {

    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "mod_type", nullable = false, length = 50)
    private String modType; // scope, silencer, magazine, barrel, stock, grip

    @Column(name = "slot_type", nullable = false, length = 50)
    private String slotType; // optics, muzzle, magazine, barrel, stock, grip

    @Column(name = "compatible_classes", columnDefinition = "TEXT")
    private String compatibleClasses; // JSON array: ["pistol", "assault_rifle"]

    @Column(name = "rarity", nullable = false, length = 20)
    private String rarity; // common, uncommon, rare, epic, legendary

    @Column(name = "bonuses", columnDefinition = "TEXT")
    private String bonuses; // JSON: {"damage": +5, "accuracy": +10}

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "available", nullable = false)
    private Boolean available = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

