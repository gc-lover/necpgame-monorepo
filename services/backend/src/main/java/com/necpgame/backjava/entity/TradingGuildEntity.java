package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.TradingGuildType;
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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "trading_guilds", indexes = {
    @Index(name = "idx_trading_guilds_name", columnList = "name", unique = true),
    @Index(name = "idx_trading_guilds_type", columnList = "guild_type"),
    @Index(name = "idx_trading_guilds_level", columnList = "level"),
    @Index(name = "idx_trading_guilds_region", columnList = "headquarters_location")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradingGuildEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false, length = 120)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "guild_type", nullable = false, length = 32)
    private TradingGuildType type;

    @Column(name = "level", nullable = false)
    private Integer level;

    @Column(name = "reputation_score", nullable = false)
    private Integer reputationScore;

    @Column(name = "member_count", nullable = false)
    private Integer memberCount;

    @Column(name = "headquarters_location", length = 160)
    private String headquartersLocation;

    @Column(name = "founder_id", nullable = false)
    private UUID founderId;

    @Column(name = "leader_id", nullable = false)
    private UUID leaderId;

    @Column(name = "leader_name", length = 120)
    private String leaderName;

    @Column(name = "total_capital", precision = 18, scale = 2, nullable = false)
    private BigDecimal totalCapital;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "specialization_json", columnDefinition = "jsonb")
    private String specializationJson;

    @Column(name = "trading_fee_reduction", precision = 6, scale = 3)
    private BigDecimal tradingFeeReduction;

    @Column(name = "profit_margin_increase", precision = 6, scale = 3)
    private BigDecimal profitMarginIncrease;

    @Column(name = "exclusive_routes_count")
    private Integer exclusiveRoutesCount;

    @Column(name = "guild_hall_level")
    private Integer guildHallLevel;

    @Column(name = "warehouse_capacity")
    private Integer warehouseCapacity;

    @Column(name = "trade_office_count")
    private Integer tradeOfficeCount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant updatedAt;
}

