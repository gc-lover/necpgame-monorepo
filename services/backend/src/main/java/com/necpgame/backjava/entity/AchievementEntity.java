package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "achievements", indexes = {
    @Index(name = "idx_achievements_category", columnList = "category"),
    @Index(name = "idx_achievements_rarity", columnList = "rarity"),
    @Index(name = "idx_achievements_type", columnList = "achievement_type")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AchievementEntity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "rarity", length = 20)
    private String rarity;

    @Column(name = "icon")
    private String icon;

    @Column(name = "requirements", columnDefinition = "JSONB")
    private String requirements;

    @Column(name = "rewards", columnDefinition = "JSONB")
    private String rewards;

    @Column(name = "points")
    private Integer points;

    @Default
    @Column(name = "is_hidden", nullable = false)
    private boolean hidden = false;

    @Default
    @Column(name = "achievement_type", length = 20)
    private String achievementType = "STANDARD";

    @Default
    @Column(name = "tier")
    private Integer tier = 1;

    @Column(name = "parent_achievement_id", columnDefinition = "UUID")
    private UUID parentAchievementId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}

