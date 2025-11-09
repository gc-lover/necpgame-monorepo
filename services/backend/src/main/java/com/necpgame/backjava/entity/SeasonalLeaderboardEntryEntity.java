package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seasonal_leaderboard_entries", indexes = {
    @Index(name = "idx_seasonal_leaderboard_category_score", columnList = "season_id, category, score DESC")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeasonalLeaderboardEntryEntity {

    @EmbeddedId
    private SeasonalLeaderboardEntryId id;

    @Column(name = "score", precision = 20, scale = 4, nullable = false)
    private BigDecimal score;

    @Column(name = "score_display", length = 160)
    private String scoreDisplay;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}

