package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leaderboard_entries", indexes = {
    @Index(name = "idx_leaderboard_entries_category_score", columnList = "category, score DESC")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaderboardEntryEntity {

    @EmbeddedId
    private LeaderboardEntryId id;

    @Column(name = "player_name", length = 120)
    private String playerName;

    @Column(name = "score", precision = 20, scale = 4, nullable = false)
    private BigDecimal score;

    @Column(name = "score_display", length = 160)
    private String scoreDisplay;

    @Column(name = "active_title", length = 120)
    private String activeTitle;

    @Column(name = "guild_id", columnDefinition = "UUID")
    private UUID guildId;

    @Column(name = "guild_name", length = 120)
    private String guildName;

    @Column(name = "guild_tag", length = 16)
    private String guildTag;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}

