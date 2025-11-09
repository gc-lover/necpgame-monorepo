package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "player_bank_slots",
    indexes = {
        @Index(name = "idx_player_bank_slots_player", columnList = "player_id"),
        @Index(name = "idx_player_bank_slots_player_slot", columnList = "player_id, slot_index", unique = true)
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerBankSlotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "player_id", nullable = false)
    private UUID playerId;

    @Column(name = "slot_index", nullable = false)
    private Integer slotIndex;

    @Column(name = "item_id", length = 100)
    private String itemId;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 0;

    @Column(name = "current_durability")
    private Integer currentDurability;

    @Column(name = "max_durability")
    private Integer maxDurability;

    @Column(name = "is_bound", nullable = false)
    private Boolean bound = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "bind_type", length = 20, nullable = false)
    private CharacterInventoryEntity.BindType bindType = CharacterInventoryEntity.BindType.NONE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", referencedColumnName = "id", insertable = false, updatable = false)
    private PlayerEntity player;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "id", insertable = false, updatable = false)
    private InventoryItemEntity item;
}


