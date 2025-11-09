package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications", indexes = {
    @Index(name = "idx_notifications_player", columnList = "player_id, is_read, created_at"),
    @Index(name = "idx_notifications_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "player_id", nullable = false, columnDefinition = "UUID")
    private UUID playerId;

    @Column(name = "type", length = 32)
    private String type;

    @Column(name = "priority", length = 16)
    private String priority;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "data", columnDefinition = "TEXT")
    private String data;

    @Column(name = "send_email")
    private boolean sendEmail;

    @Column(name = "is_read")
    private boolean read;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}

