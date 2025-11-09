package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Композитный идентификатор для CharacterSlotEntity.
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterSlotId implements Serializable {

    @Column(name = "player_id", nullable = false)
    private UUID playerId;

    @Column(name = "slot_number", nullable = false)
    private Integer slotNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CharacterSlotId that = (CharacterSlotId) o;
        return Objects.equals(playerId, that.playerId)
            && Objects.equals(slotNumber, that.slotNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, slotNumber);
    }
}

