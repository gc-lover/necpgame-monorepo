package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

/**
 * Entity для хранения лута, выпавшего в мире
 * Соответствует схеме loot-system.yaml
 */
@Entity
@Table(name = "world_drops", indexes = {
    @Index(name = "idx_world_drops_expires_at", columnList = "expires_at"),
    @Index(name = "idx_world_drops_source", columnList = "source_type, source_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LootDropEntity {

    @Id
    @Column(name = "drop_id", length = 64)
    private String dropId;

    @Column(name = "source_type", length = 50, nullable = false)
    private String sourceType; // npc_death, container, boss, quest_reward

    @Column(name = "source_id", length = 64, nullable = false)
    private String sourceId;

    @Column(name = "position_x")
    private Double positionX;

    @Column(name = "position_y")
    private Double positionY;

    @Column(name = "position_z")
    private Double positionZ;

    @Column(name = "party_id", length = 64)
    private String partyId;

    @Column(name = "loot_mode", length = 50)
    private String lootMode; // personal, shared, need_greed, master_looter

    @Column(name = "items_json", columnDefinition = "TEXT")
    private String itemsJson; // JSON массив предметов

    @Column(name = "looted", nullable = false)
    private Boolean looted = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;
}

