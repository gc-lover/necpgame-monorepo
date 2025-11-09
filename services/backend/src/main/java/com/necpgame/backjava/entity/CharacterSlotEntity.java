package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

/**
 * CharacterSlotEntity — слот персонажа игрока (3 базовых + 2 премиум).
 */
@Entity
@Table(
    name = "character_slots",
    uniqueConstraints = @UniqueConstraint(name = "uc_character_slots_character", columnNames = "character_id")
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterSlotEntity {

    public enum SlotType {
        BASE,
        PREMIUM
    }

    @EmbeddedId
    private CharacterSlotId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false, insertable = false, updatable = false)
    private PlayerEntity player;

    @Enumerated(EnumType.STRING)
    @Column(name = "slot_type", nullable = false, length = 16)
    private SlotType slotType;

    @Column(name = "is_unlocked", nullable = false)
    private Boolean unlocked = Boolean.TRUE;

    @Column(name = "character_id")
    private UUID characterId;

    @Column(name = "reserved_until")
    private OffsetDateTime reservedUntil;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public boolean isFree() {
        return Boolean.TRUE.equals(unlocked) && characterId == null;
    }

    public void assignCharacter(UUID newCharacterId) {
        this.characterId = newCharacterId;
        this.reservedUntil = null;
    }

    public void releaseSlot(OffsetDateTime reservation) {
        this.characterId = null;
        this.reservedUntil = reservation;
    }
}

