package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.LogisticsEscortType;
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

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "logistics_escort_requests")
public class LogisticsEscortRequestEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private LogisticsShipmentEntity shipment;

    @Enumerated(EnumType.STRING)
    @Column(name = "escort_type", nullable = false, length = 16)
    private LogisticsEscortType escortType;

    @Column(name = "payment")
    private Integer payment;

    @Column(name = "requested_at", nullable = false)
    private OffsetDateTime requestedAt;
}
