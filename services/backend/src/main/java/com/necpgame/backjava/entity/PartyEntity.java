package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

/**
 * Entity для групп (party)
 * Соответствует схеме party-system.yaml
 */
@Entity
@Table(name = "parties", indexes = {
    @Index(name = "idx_parties_leader", columnList = "leader_character_id"),
    @Index(name = "idx_parties_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyEntity {

    @Id
    @Column(name = "party_id", length = 64)
    private String partyId;

    @Column(name = "leader_character_id", length = 64, nullable = false)
    private String leaderCharacterId;

    @Column(name = "members_json", columnDefinition = "TEXT")
    private String membersJson; // JSON array of character IDs

    @Column(name = "max_members")
    private Integer maxMembers = 5;

    @Column(name = "loot_mode", length = 50)
    private String lootMode; // personal, shared, need_greed, master_looter

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}

