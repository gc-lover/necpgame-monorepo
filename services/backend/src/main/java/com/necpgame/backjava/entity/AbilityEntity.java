package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * AbilityEntity - способности (справочник).
 * 
 * Справочник всех способностей в стиле VALORANT (Q/E/R структура).
 * Источник: API-SWAGGER/api/v1/gameplay/combat/abilities.yaml (Ability schema)
 */
@Entity
@Table(name = "abilities", indexes = {
    @Index(name = "idx_abilities_slot", columnList = "slot"),
    @Index(name = "idx_abilities_source", columnList = "source_type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AbilityEntity {

    @Id
    @Column(name = "id", length = 100, nullable = false)
    private String id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", length = 2000)
    private String description;

    @Column(name = "slot", nullable = false, length = 10)
    private String slot; // Q, E, R, C, X

    @Column(name = "source_type", nullable = false, length = 50)
    private String sourceType; // equipment, implants, skills, cyberdeck

    @Column(name = "source_id", length = 100)
    private String sourceId; // ID экипировки/импланта/навыка

    @Column(name = "cooldown", nullable = false)
    private Integer cooldown; // секунды

    @Column(name = "energy_cost")
    private Integer energyCost;

    @Column(name = "health_cost")
    private Integer healthCost;

    @Column(name = "charges")
    private Integer charges; // количество зарядов (null = unlimited)

    @Column(name = "effects", columnDefinition = "TEXT")
    private String effects; // JSON array: [{type: "damage", value: 50}]

    @Column(name = "duration")
    private Integer duration; // секунды (для длящихся эффектов)

    @Column(name = "range")
    private Integer range; // метры

    @Column(name = "area_of_effect")
    private Integer areaOfEffect; // метры (для AoE)

    @Column(name = "min_level")
    private Integer minLevel = 1;

    @Column(name = "available", nullable = false)
    private Boolean available = true;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

