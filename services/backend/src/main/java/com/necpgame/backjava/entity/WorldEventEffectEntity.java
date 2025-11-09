package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.EventEffectType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
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
@Table(name = "world_event_effects", indexes = {
        @Index(name = "idx_world_event_effects_event", columnList = "event_id"),
        @Index(name = "idx_world_event_effects_character", columnList = "effect_id")
})
public class WorldEventEffectEntity {

    @Id
    @Column(name = "effect_id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private WorldEventEntity event;

    @Enumerated(EnumType.STRING)
    @Column(name = "effect_type", nullable = false, length = 32)
    private EventEffectType effectType;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "modifier_type", length = 64)
    private String modifierType;

    @Column(name = "modifier_value", precision = 10, scale = 2)
    private BigDecimal modifierValue;

    @Column(name = "duration", length = 128)
    private String duration;

    @Column(name = "is_stackable", nullable = false)
    private boolean stackable;
}

