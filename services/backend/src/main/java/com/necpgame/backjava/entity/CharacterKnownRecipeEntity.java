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
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/**
 * CharacterKnownRecipeEntity - изученные рецепты персонажа.
 */
@Entity
@Table(name = "character_known_recipes", indexes = {
    @Index(name = "idx_character_known_recipes_character", columnList = "character_id"),
    @Index(name = "idx_character_known_recipes_recipe", columnList = "recipe_id"),
    @Index(name = "idx_character_known_recipes_unique", columnList = "character_id, recipe_id", unique = true)
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CharacterKnownRecipeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Column(name = "recipe_id", nullable = false, length = 120)
    private String recipeId;

    @CreationTimestamp
    @Column(name = "learned_at", nullable = false)
    private LocalDateTime learnedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", referencedColumnName = "recipe_id", insertable = false, updatable = false)
    private CraftingRecipeEntity recipe;
}

