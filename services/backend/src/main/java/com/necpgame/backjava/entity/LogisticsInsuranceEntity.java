package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.LogisticsInsurancePlan;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "logistics_insurance")
public class LogisticsInsuranceEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false, unique = true)
    private LogisticsShipmentEntity shipment;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false, length = 16)
    private LogisticsInsurancePlan plan;

    @Column(name = "coverage_percentage", nullable = false)
    private Integer coveragePercentage;

    @Column(name = "max_coverage", nullable = false)
    private Integer maxCoverage;

    @Column(name = "cost", nullable = false)
    private Integer cost;

    @Column(name = "purchased_at", nullable = false)
    private OffsetDateTime purchasedAt;

    @Column(name = "total_cargo_value", precision = 14, scale = 2)
    private BigDecimal totalCargoValue;
}

