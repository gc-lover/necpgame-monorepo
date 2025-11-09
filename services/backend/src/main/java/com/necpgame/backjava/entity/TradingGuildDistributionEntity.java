package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.DistributionType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trading_guild_distributions", indexes = {
    @Index(name = "idx_trading_guild_distributions_guild", columnList = "guild_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradingGuildDistributionEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "guild_id", nullable = false)
    private UUID guildId;

    @Enumerated(EnumType.STRING)
    @Column(name = "distribution_type", nullable = false, length = 24)
    private DistributionType distributionType;

    @Column(name = "total_amount", precision = 18, scale = 2, nullable = false)
    private BigDecimal totalAmount;

    @Column(name = "details_json", columnDefinition = "jsonb")
    private String detailsJson;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdAt;
}

