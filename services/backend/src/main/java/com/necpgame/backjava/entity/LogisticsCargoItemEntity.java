package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "logistics_shipment_cargo_items")
public class LogisticsCargoItemEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", nullable = false)
    private LogisticsShipmentEntity shipment;

    @Column(name = "item_id", nullable = false)
    private String itemId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "weight", precision = 10, scale = 2)
    private BigDecimal weight;

    @Column(name = "volume", precision = 10, scale = 2)
    private BigDecimal volume;

    @Column(name = "value", precision = 14, scale = 2)
    private BigDecimal value;

    @Column(name = "fragile", nullable = false)
    private boolean fragile;
}

