package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.TradeRouteDangerLevel;
import com.necpgame.backjava.entity.enums.TradeRoutePermissionLevel;
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
@Table(name = "trading_guild_routes", indexes = {
    @Index(name = "idx_trading_guild_routes_guild", columnList = "guild_id"),
    @Index(name = "idx_trading_guild_routes_permission", columnList = "permission_level")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradingGuildRouteEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "guild_id", nullable = false)
    private UUID guildId;

    @Column(name = "route_id", nullable = false)
    private UUID routeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission_level", nullable = false, length = 16)
    private TradeRoutePermissionLevel permissionLevel;

    @Column(name = "name", length = 180)
    private String name;

    @Column(name = "origin", length = 160)
    private String origin;

    @Column(name = "destination", length = 160)
    private String destination;

    @Column(name = "goods_json", columnDefinition = "jsonb")
    private String goodsJson;

    @Column(name = "profit_margin", precision = 6, scale = 3)
    private BigDecimal profitMargin;

    @Column(name = "exclusive", nullable = false)
    private boolean exclusive;

    @Enumerated(EnumType.STRING)
    @Column(name = "danger_level", length = 16)
    private TradeRouteDangerLevel dangerLevel;

    @Column(name = "obtained_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant obtainedAt;

    @Column(name = "expires_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant expiresAt;
}

