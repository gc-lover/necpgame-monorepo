package com.necpgame.workqueue.domain.npc;

import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "npc_inventory_items")
@Getter
@Setter
@NoArgsConstructor
public class NpcInventoryItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "npc_entity_id")
    private ContentEntryEntity npc;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_entity_id")
    private ContentEntryEntity item;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "restock_interval_minutes")
    private Integer restockIntervalMinutes;

    @Column(name = "price_override")
    private BigDecimal priceOverride;

    @Column(name = "metadata", columnDefinition = "JSONB", nullable = false)
    private String metadataJson;
}

