package com.necpgame.workqueue.domain.item;

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
@Table(name = "consumable_effects")
@Getter
@Setter
@NoArgsConstructor
public class ConsumableEffectEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_entity_id")
    private ContentEntryEntity item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "effect_entity_id")
    private ContentEntryEntity effectEntity;

    @Column(name = "duration_seconds")
    private BigDecimal durationSeconds;

    @Column(name = "cooldown_seconds")
    private BigDecimal cooldownSeconds;

    @Column(name = "metadata", columnDefinition = "JSONB", nullable = false)
    private String metadata;
}

