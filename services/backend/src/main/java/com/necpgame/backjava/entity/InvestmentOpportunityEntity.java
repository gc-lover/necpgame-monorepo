package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
@Table(name = "investment_opportunities", indexes = {
    @Index(name = "idx_investment_opportunities_type", columnList = "type"),
    @Index(name = "idx_investment_opportunities_risk_level", columnList = "risk_level")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentOpportunityEntity {

    public enum OpportunityType {
        CORPORATE,
        FACTION,
        REGIONAL,
        REAL_ESTATE,
        PRODUCTION_CHAINS
    }

    public enum RiskLevel {
        LOW,
        MEDIUM,
        HIGH,
        VERY_HIGH
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 180)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 32)
    private OpportunityType type;

    @Column(name = "description", nullable = false, columnDefinition = "text")
    private String description;

    @Column(name = "min_investment", nullable = false, precision = 18, scale = 2)
    private BigDecimal minInvestment;

    @Column(name = "max_investment", precision = 18, scale = 2)
    private BigDecimal maxInvestment;

    @Column(name = "expected_roi_percent", nullable = false, precision = 6, scale = 2)
    private BigDecimal expectedRoiPercent;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false, length = 16)
    private RiskLevel riskLevel;

    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;

    @Column(name = "available_slots", nullable = false)
    private Integer availableSlots;

    @Column(name = "slots_total", nullable = false)
    private Integer slotsTotal;

    @Column(name = "last_updated_value_at")
    private OffsetDateTime lastUpdatedValueAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public boolean hasAvailableSlots() {
        return availableSlots != null && availableSlots > 0;
    }

    public void allocateSlot() {
        if (!hasAvailableSlots()) {
            throw new IllegalStateException("No available slots for opportunity " + id);
        }
        availableSlots = availableSlots - 1;
    }

    public void releaseSlot() {
        if (availableSlots == null) {
            availableSlots = 0;
        }
        if (slotsTotal != null && availableSlots < slotsTotal) {
            availableSlots = availableSlots + 1;
        }
    }
}


