package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.GuildTransactionType;
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
@Table(name = "trading_guild_transactions", indexes = {
    @Index(name = "idx_trading_guild_transactions_guild", columnList = "guild_id"),
    @Index(name = "idx_trading_guild_transactions_type", columnList = "transaction_type")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TradingGuildBankTransactionEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "guild_id", nullable = false)
    private UUID guildId;

    @Column(name = "performed_by")
    private UUID performedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", nullable = false, length = 32)
    private GuildTransactionType transactionType;

    @Column(name = "amount", precision = 18, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "currency", length = 16, nullable = false)
    private String currency;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "metadata_json", columnDefinition = "jsonb")
    private String metadataJson;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdAt;
}

