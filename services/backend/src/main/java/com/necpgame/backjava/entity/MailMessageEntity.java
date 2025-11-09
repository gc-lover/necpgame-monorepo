package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "mail_messages", indexes = {
    @Index(name = "idx_mail_messages_recipient", columnList = "recipient_character_id, is_deleted, sent_at"),
    @Index(name = "idx_mail_messages_expires", columnList = "expires_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MailMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sender_character_id")
    private UUID senderCharacterId;

    @Column(name = "recipient_character_id", nullable = false)
    private UUID recipientCharacterId;

    @Column(name = "recipient_character_name", length = 200)
    private String recipientCharacterName;

    @Column(name = "mail_type", length = 50, nullable = false)
    private String mailType;

    @Column(name = "subject", length = 200, nullable = false)
    private String subject;

    @Column(name = "body", columnDefinition = "TEXT")
    private String body;

    @Column(name = "attached_items", columnDefinition = "TEXT")
    private String attachedItems;

    @Column(name = "attached_gold")
    private Long attachedGold;

    @Column(name = "cod_amount")
    private Long codAmount;

    @Column(name = "is_read")
    private boolean read;

    @Column(name = "is_claimed")
    private boolean claimed;

    @Column(name = "is_deleted")
    private boolean deleted;

    @CreationTimestamp
    @Column(name = "sent_at", nullable = false, updatable = false)
    private OffsetDateTime sentAt;

    @Column(name = "read_at")
    private OffsetDateTime readAt;

    @Column(name = "claimed_at")
    private OffsetDateTime claimedAt;

    @Column(name = "expires_at", nullable = false)
    private OffsetDateTime expiresAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "returned")
    private boolean returned;

    @Column(name = "returned_at")
    private OffsetDateTime returnedAt;

    @PrePersist
    void onCreate() {
        if (mailType == null) {
            mailType = "PLAYER";
        }
        if (attachedItems == null) {
            attachedItems = "[]";
        }
        if (attachedGold == null) {
            attachedGold = 0L;
        }
        if (codAmount == null) {
            codAmount = 0L;
        }
        if (expiresAt == null) {
            expiresAt = OffsetDateTime.now().plusDays(30);
        }
    }
}

