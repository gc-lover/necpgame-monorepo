package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import org.hibernate.annotations.UpdateTimestamp;

/**
 * CraftingSessionEntity - активные и завершенные сессии крафта.
 */
@Entity
@Table(name = "crafting_sessions", indexes = {
    @Index(name = "idx_crafting_sessions_character", columnList = "character_id"),
    @Index(name = "idx_crafting_sessions_recipe", columnList = "recipe_id"),
    @Index(name = "idx_crafting_sessions_status", columnList = "status")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CraftingSessionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "session_id")
    private UUID sessionId;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Column(name = "recipe_id", nullable = false, length = 120)
    private String recipeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 24, nullable = false)
    private CraftingStatus status;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "completed_count", nullable = false)
    private Integer completedCount;

    @Column(name = "success_chance", precision = 6, scale = 4)
    private Double successChance;

    @Column(name = "base_time_seconds")
    private Integer baseTimeSeconds;

    @Column(name = "final_time_seconds")
    private Integer finalTimeSeconds;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "estimated_completion")
    private LocalDateTime estimatedCompletion;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "boosts_json", columnDefinition = "jsonb")
    private String boostsJson;

    @Column(name = "items_crafted_json", columnDefinition = "jsonb")
    private String itemsCraftedJson;

    @Column(name = "components_consumed_json", columnDefinition = "jsonb")
    private String componentsConsumedJson;

    @Column(name = "failure_reason", columnDefinition = "TEXT")
    private String failureReason;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", referencedColumnName = "recipe_id", insertable = false, updatable = false)
    private CraftingRecipeEntity recipe;

    public enum CraftingStatus {
        IN_PROGRESS,
        COMPLETED,
        FAILED,
        CANCELLED
    }
}

