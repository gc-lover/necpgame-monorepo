package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

/**
 * Entity для друзей
 * Соответствует схеме friend-system.yaml
 */
@Entity
@Table(name = "friendships", indexes = {
    @Index(name = "idx_friendships_character1", columnList = "character_id_1"),
    @Index(name = "idx_friendships_character2", columnList = "character_id_2"),
    @Index(name = "idx_friendships_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipEntity {

    @Id
    @Column(name = "friendship_id", length = 64)
    private String friendshipId;

    @Column(name = "character_id_1", length = 64, nullable = false)
    private String characterId1; // Инициатор

    @Column(name = "character_id_2", length = 64, nullable = false)
    private String characterId2; // Получатель

    @Column(name = "status", length = 20, nullable = false)
    private String status; // pending, accepted, declined, blocked

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "accepted_at")
    private OffsetDateTime acceptedAt;
}

