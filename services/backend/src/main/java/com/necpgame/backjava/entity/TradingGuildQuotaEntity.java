package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trading_guild_quotas", indexes = {
    @Index(name = "idx_trading_guild_quotas_guild", columnList = "guild_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradingGuildQuotaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "guild_id", nullable = false)
    private UUID guildId;

    @Column(name = "item_category", length = 160, nullable = false)
    private String itemCategory;

    @Column(name = "max_quantity_per_week", nullable = false)
    private Integer maxQuantityPerWeek;

    @Column(name = "current_used", nullable = false)
    private Integer currentUsed;

    @Column(name = "resets_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant resetsAt;
}

