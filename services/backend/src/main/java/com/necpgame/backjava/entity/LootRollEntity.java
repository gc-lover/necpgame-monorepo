package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

/**
 * Entity для хранения роллов на предметы
 * Соответствует схеме loot-system.yaml
 */
@Entity
@Table(name = "loot_rolls", indexes = {
    @Index(name = "idx_loot_rolls_drop_id", columnList = "drop_id"),
    @Index(name = "idx_loot_rolls_character_id", columnList = "character_id"),
    @Index(name = "idx_loot_rolls_expires_at", columnList = "expires_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LootRollEntity {

    @Id
    @Column(name = "roll_id", length = 64)
    private String rollId;

    @Column(name = "drop_id", length = 64, nullable = false)
    private String dropId;

    @Column(name = "item_id", length = 64, nullable = false)
    private String itemId;

    @Column(name = "character_id", length = 64, nullable = false)
    private String characterId;

    @Column(name = "roll_type", length = 20, nullable = false)
    private String rollType; // need, greed, pass

    @Column(name = "roll_value")
    private Integer rollValue; // 1-100

    @Column(name = "winner_id", length = 64)
    private String winnerId;

    @Column(name = "status", length = 20, nullable = false)
    private String status; // active, completed, expired

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt; // created_at + 60s
}

