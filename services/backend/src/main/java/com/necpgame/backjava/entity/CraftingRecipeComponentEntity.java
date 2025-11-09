package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CraftingRecipeComponentEntity - требования к компонентам рецепта.
 */
@Entity
@Table(name = "crafting_recipe_components", indexes = {
    @Index(name = "idx_crafting_recipe_components_recipe", columnList = "recipe_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CraftingRecipeComponentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "recipe_id", nullable = false, length = 120)
    private String recipeId;

    @Column(name = "component_id", nullable = false, length = 120)
    private String componentId;

    @Column(name = "component_name", nullable = false, length = 200)
    private String componentName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "rarity", length = 20)
    private String rarity;

    @Column(name = "alternatives_json", columnDefinition = "jsonb")
    private String alternativesJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", referencedColumnName = "recipe_id", insertable = false, updatable = false)
    private CraftingRecipeEntity recipe;
}

