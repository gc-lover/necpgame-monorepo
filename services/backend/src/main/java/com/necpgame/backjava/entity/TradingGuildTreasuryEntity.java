package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "trading_guild_treasuries")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradingGuildTreasuryEntity {

    @Id
    @Column(name = "guild_id", nullable = false)
    private UUID guildId;

    @Column(name = "balance", precision = 18, scale = 2, nullable = false)
    private BigDecimal balance;

    @Column(name = "currencies_json", columnDefinition = "jsonb")
    private String currenciesJson;

    @Column(name = "assets_json", columnDefinition = "jsonb")
    private String assetsJson;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant updatedAt;
}

