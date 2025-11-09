package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.LogisticsConvoyStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
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
@Table(name = "logistics_convoys")
public class LogisticsConvoyEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "leader_id", nullable = false)
    private UUID leaderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private LogisticsConvoyStatus status;

    @Column(name = "risk_reduction", precision = 6, scale = 2)
    private BigDecimal riskReduction;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @OneToMany(mappedBy = "convoy", fetch = FetchType.LAZY)
    private List<LogisticsConvoyMemberEntity> members = new ArrayList<>();

    @OneToMany(mappedBy = "convoy", fetch = FetchType.LAZY)
    private List<LogisticsConvoyShipmentEntity> shipments = new ArrayList<>();
}
