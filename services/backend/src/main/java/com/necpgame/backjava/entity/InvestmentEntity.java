package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "character_investments", indexes = {
    @Index(name = "idx_character_investments_character", columnList = "character_id"),
    @Index(name = "idx_character_investments_status", columnList = "status")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentEntity {

    public enum InvestmentStatus {
        ACTIVE,
        MATURED,
        WITHDRAWN,
        FAILED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "opportunity_id", nullable = false)
    private InvestmentOpportunityEntity opportunity;

    @Column(name = "amount_invested", nullable = false, precision = 18, scale = 2)
    private BigDecimal amountInvested;

    @Column(name = "current_value", nullable = false, precision = 18, scale = 2)
    private BigDecimal currentValue;

    @Column(name = "expected_roi_percent", nullable = false, precision = 6, scale = 2)
    private BigDecimal expectedRoiPercent;

    @Column(name = "dividends_total", nullable = false, precision = 18, scale = 2)
    private BigDecimal dividendsTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private InvestmentStatus status;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "maturity_date")
    private OffsetDateTime maturityDate;

    @Column(name = "last_evaluated_at")
    private OffsetDateTime lastEvaluatedAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public BigDecimal getProfitLoss() {
        return currentValue.subtract(amountInvested);
    }
}


