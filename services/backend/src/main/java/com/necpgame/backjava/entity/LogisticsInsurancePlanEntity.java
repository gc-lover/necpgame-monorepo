package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.LogisticsInsurancePlan;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "logistics_insurance_plans")
public class LogisticsInsurancePlanEntity {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false, length = 16)
    private LogisticsInsurancePlan plan;

    @Column(name = "coverage_percentage", nullable = false)
    private Integer coveragePercentage;

    @Column(name = "max_coverage", nullable = false)
    private Integer maxCoverage;

    @Column(name = "cost_percentage", precision = 6, scale = 3, nullable = false)
    private BigDecimal costPercentage;

    @Column(name = "description", columnDefinition = "text")
    private String description;
}

