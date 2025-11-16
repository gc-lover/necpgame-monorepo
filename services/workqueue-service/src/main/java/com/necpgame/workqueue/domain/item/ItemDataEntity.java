package com.necpgame.workqueue.domain.item;

import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "item_data")
@Getter
@Setter
@NoArgsConstructor
public class ItemDataEntity {
    @Id
    @Column(name = "content_entity_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "content_entity_id")
    private ContentEntryEntity entity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_category_value_id")
    private EnumValueEntity category;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_slot_value_id")
    private EnumValueEntity slot;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rarity_value_id")
    private EnumValueEntity rarity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bind_type_value_id")
    private EnumValueEntity bindType;

    @Column(name = "weight")
    private BigDecimal weight;

    @Column(name = "level_requirement")
    private Integer levelRequirement;

    @Column(name = "stack_size")
    private Integer stackSize;

    @Column(name = "vendor_price")
    private BigDecimal vendorPrice;

    @Column(name = "durability_max")
    private Integer durabilityMax;

    @Column(name = "is_tradeable", nullable = false)
    private boolean tradeable;

    @Column(name = "power_score")
    private BigDecimal powerScore;

    @Column(name = "metadata", columnDefinition = "JSONB", nullable = false)
    private String metadata;
}

