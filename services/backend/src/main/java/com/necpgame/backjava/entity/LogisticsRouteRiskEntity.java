package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.LogisticsRiskSeverity;
import com.necpgame.backjava.entity.enums.LogisticsRouteRiskType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "logistics_route_risks")
public class LogisticsRouteRiskEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private LogisticsRouteEntity route;

    @Enumerated(EnumType.STRING)
    @Column(name = "risk_type", nullable = false, length = 32)
    private LogisticsRouteRiskType riskType;

    @Column(name = "probability", precision = 5, scale = 2)
    private BigDecimal probability;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", length = 16)
    private LogisticsRiskSeverity severity;

    @Column(name = "description", columnDefinition = "text")
    private String description;
}

