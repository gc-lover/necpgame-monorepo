package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "investment_funds", indexes = {
    @Index(name = "idx_investment_funds_type", columnList = "type")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvestmentFundEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 160)
    private String name;

    @Column(name = "manager", nullable = false, length = 160)
    private String manager;

    @Column(name = "type", nullable = false, length = 64)
    private String type;

    @Column(name = "total_value", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalValue;

    @Column(name = "investors_count", nullable = false)
    private Integer investorsCount;

    @Column(name = "performance_ytd", nullable = false, precision = 6, scale = 2)
    private BigDecimal performanceYtd;

    @Column(name = "min_investment", nullable = false, precision = 18, scale = 2)
    private BigDecimal minInvestment;

    @Column(name = "management_fee_percent", nullable = false, precision = 6, scale = 2)
    private BigDecimal managementFeePercent;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}


