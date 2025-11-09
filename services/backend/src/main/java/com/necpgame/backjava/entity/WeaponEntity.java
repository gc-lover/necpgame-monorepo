package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * WeaponEntity - оружие (справочник).
 * 
 * Справочник всего оружия в игре (80+ моделей из лора Cyberpunk).
 * Источник: API-SWAGGER/api/v1/gameplay/combat/weapons.yaml (WeaponDetails schema)
 */
@Entity
@Table(name = "weapons", indexes = {
    @Index(name = "idx_weapons_class", columnList = "weapon_class"),
    @Index(name = "idx_weapons_brand", columnList = "brand"),
    @Index(name = "idx_weapons_rarity", columnList = "rarity")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeaponEntity {

    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "weapon_class", nullable = false, length = 50)
    private String weaponClass; // pistol, assault_rifle, shotgun, sniper_rifle, smg, lmg, melee, cyberware

    @Column(name = "brand", length = 100)
    private String brand; // arasaka, militech, kang_tao, budget_arms, constitutional_arms

    @Column(name = "rarity", nullable = false, length = 20)
    private String rarity; // common, uncommon, rare, epic, legendary, exotic

    @Column(name = "damage", nullable = false)
    private Integer damage;

    @Column(name = "fire_rate")
    private Integer fireRate; // выстрелов в минуту

    @Column(name = "reload_time")
    private Integer reloadTime; // секунды

    @Column(name = "magazine_size")
    private Integer magazineSize;

    @Column(name = "range")
    private Integer range; // метры

    @Column(name = "accuracy", nullable = false)
    private Integer accuracy; // 0-100

    @Column(name = "crit_chance")
    private Integer critChance; // 0-100

    @Column(name = "crit_damage")
    private Integer critDamage; // процент

    @Column(name = "mod_slots")
    private Integer modSlots = 0;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "min_level", nullable = false)
    private Integer minLevel = 1;

    @Column(name = "required_stats", length = 500)
    private String requiredStats; // JSON: {"strength": 5, "reflexes": 8}

    @Column(name = "special_abilities", columnDefinition = "TEXT")
    private String specialAbilities; // JSON array

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "is_cyberware", nullable = false)
    private Boolean isCyberware = false;

    @Column(name = "available", nullable = false)
    private Boolean available = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

