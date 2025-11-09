package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "world_event_character_effects", indexes = {
        @Index(name = "idx_world_event_character", columnList = "character_id"),
        @Index(name = "idx_world_event_effect", columnList = "effect_id")
})
public class WorldEventCharacterEffectEntity {

    @EmbeddedId
    private WorldEventCharacterEffectId id;

    @MapsId("effectId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "effect_id", nullable = false)
    private WorldEventEffectEntity effect;

    @Column(name = "applied_at", nullable = false)
    private OffsetDateTime appliedAt;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;
}

