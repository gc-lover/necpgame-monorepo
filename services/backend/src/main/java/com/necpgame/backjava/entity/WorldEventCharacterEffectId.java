package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class WorldEventCharacterEffectId implements Serializable {

    @Column(name = "effect_id", nullable = false)
    private UUID effectId;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;
}

