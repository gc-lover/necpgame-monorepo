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
@Table(name = "weapon_stats")
@Getter
@Setter
@NoArgsConstructor
public class WeaponStatsEntity {
    @Id
    @Column(name = "item_entity_id")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "item_entity_id")
    private ContentEntryEntity item;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weapon_class_value_id")
    private EnumValueEntity weaponClass;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "damage_type_value_id")
    private EnumValueEntity damageType;

    @Column(name = "damage_min")
    private BigDecimal damageMin;

    @Column(name = "damage_max")
    private BigDecimal damageMax;

    @Column(name = "fire_rate")
    private BigDecimal fireRate;

    @Column(name = "magazine_size")
    private Integer magazineSize;

    @Column(name = "reload_time_seconds")
    private BigDecimal reloadTimeSeconds;

    @Column(name = "range_min")
    private BigDecimal rangeMin;

    @Column(name = "range_max")
    private BigDecimal rangeMax;

    @Column(name = "critical_chance")
    private BigDecimal criticalChance;

    @Column(name = "critical_multiplier")
    private BigDecimal criticalMultiplier;

    @Column(name = "accuracy")
    private BigDecimal accuracy;

    @Column(name = "recoil")
    private BigDecimal recoil;

    @Column(name = "metadata", columnDefinition = "JSONB", nullable = false)
    private String metadata;
}

