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
@Table(name = "armor_stats")
@Getter
@Setter
@NoArgsConstructor
public class ArmorStatsEntity {
    @Id
    @Column(name = "item_entity_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "item_entity_id")
    private ContentEntryEntity item;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "armor_type_value_id")
    private EnumValueEntity armorType;

    @Column(name = "armor_value")
    private BigDecimal armorValue;

    @Column(name = "resistances", columnDefinition = "JSONB")
    private String resistances;

    @Column(name = "mobility_penalty")
    private BigDecimal mobilityPenalty;

    @Column(name = "metadata", columnDefinition = "JSONB", nullable = false)
    private String metadata;
}

