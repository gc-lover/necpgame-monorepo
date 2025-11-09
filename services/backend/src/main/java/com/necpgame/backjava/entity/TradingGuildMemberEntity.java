package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.TradingGuildRole;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trading_guild_members", indexes = {
    @Index(name = "idx_trading_guild_members_role", columnList = "role")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradingGuildMemberEntity {

    @EmbeddedId
    private TradingGuildMemberId id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 32)
    private TradingGuildRole role;

    @Column(name = "character_name", length = 160)
    private String characterName;

    @Column(name = "contribution_total", precision = 18, scale = 2, nullable = false)
    private BigDecimal contributionTotal;

    @Column(name = "voting_power", precision = 6, scale = 2, nullable = false)
    private BigDecimal votingPower;

    @Column(name = "trades_completed")
    private Integer tradesCompleted;

    @Column(name = "joined_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant joinedAt;
}

