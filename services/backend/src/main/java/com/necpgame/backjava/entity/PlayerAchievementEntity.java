package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "player_achievements", indexes = {
    @Index(name = "idx_player_achievements_player", columnList = "player_id"),
    @Index(name = "idx_player_achievements_status", columnList = "player_id,status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerAchievementEntity {

    @EmbeddedId
    private PlayerAchievementId id;

    @MapsId("achievementId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "achievement_id", nullable = false)
    private AchievementEntity achievement;

    @Default
    @Column(name = "status", length = 20, nullable = false)
    private String status = "LOCKED";

    @Default
    @Column(name = "current_progress", nullable = false)
    private Integer currentProgress = 0;

    @Column(name = "target_progress")
    private Integer targetProgress;

    @Default
    @Column(name = "progress_percent", precision = 5, scale = 2)
    private BigDecimal progressPercent = BigDecimal.ZERO;

    @Default
    @Column(name = "reward_claimed", nullable = false)
    private boolean rewardClaimed = false;

    @Column(name = "unlocked_at")
    private OffsetDateTime unlockedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}

