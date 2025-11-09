package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * CraftingRecipeEntity - справочник рецептов крафта.
 *
 * Источник: API-SWAGGER/api/v1/gameplay/economy/crafting-system/crafting-system.yaml
 */
@Entity
@Table(name = "crafting_recipes", indexes = {
    @Index(name = "idx_crafting_recipes_category", columnList = "category"),
    @Index(name = "idx_crafting_recipes_tier", columnList = "tier"),
    @Index(name = "idx_crafting_recipes_skill_level", columnList = "required_skill_level")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CraftingRecipeEntity {

    @Id
    @Column(name = "recipe_id", nullable = false, length = 120)
    private String recipeId;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category", length = 40, nullable = false)
    private String category;

    @Column(name = "tier", length = 10, nullable = false)
    private String tier;

    @Column(name = "required_skill", length = 60, nullable = false)
    private String requiredSkill;

    @Column(name = "required_skill_level", nullable = false)
    private Integer requiredSkillLevel;

    @Column(name = "base_crafting_time_seconds", nullable = false)
    private Integer baseCraftingTimeSeconds;

    @Column(name = "base_success_rate", precision = 6, scale = 4, nullable = false)
    private Double baseSuccessRate;

    @Column(name = "components_count")
    private Integer componentsCount;

    @Column(name = "station_requirement", length = 100)
    private String stationRequirement;

    @Column(name = "unlock_source", length = 200)
    private String unlockSource;

    @Column(name = "result_item_id", length = 120)
    private String resultItemId;

    @Column(name = "result_item_name", length = 200)
    private String resultItemName;

    @Column(name = "result_quality_min", length = 20)
    private String resultQualityMin;

    @Column(name = "result_quality_max", length = 20)
    private String resultQualityMax;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}

