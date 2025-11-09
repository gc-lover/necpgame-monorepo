package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.LogisticsIncidentType;
import com.necpgame.backjava.entity.enums.LogisticsRiskSeverity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "logistics_incidents")
public class LogisticsIncidentEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private LogisticsShipmentEntity shipment;

    @Enumerated(EnumType.STRING)
    @Column(name = "incident_type", nullable = false, length = 32)
    private LogisticsIncidentType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", length = 16)
    private LogisticsRiskSeverity severity;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "resolved", nullable = false)
    private boolean resolved;

    @Column(name = "insurance_claim", nullable = false)
    private boolean insuranceClaim;

    @Column(name = "occurred_at", nullable = false)
    private OffsetDateTime occurredAt;

    @OneToMany(mappedBy = "incident", fetch = FetchType.LAZY)
    private List<LogisticsIncidentCargoLossEntity> cargoLosses = new ArrayList<>();
}
