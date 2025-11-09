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
@Table(name = "guild_leaderboard_entries", indexes = {
    @Index(name = "idx_guild_leaderboard_category_score", columnList = "category, score DESC")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuildLeaderboardEntryEntity {

    @EmbeddedId
    private GuildLeaderboardEntryId id;

    @Column(name = "guild_name", length = 160)
    private String guildName;

    @Column(name = "guild_tag", length = 16)
    private String guildTag;

    @Column(name = "score", precision = 20, scale = 4, nullable = false)
    private BigDecimal score;

    @Column(name = "score_display", length = 160)
    private String scoreDisplay;

    @Column(name = "member_count")
    private Integer memberCount;

    @Column(name = "leader_name", length = 120)
    private String leaderName;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}

