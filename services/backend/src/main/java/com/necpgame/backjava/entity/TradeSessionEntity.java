package com.necpgame.backjava.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;

/**
 * Entity для торговых сессий между игроками
 * Соответствует схеме trade-system.yaml
 */
@Entity
@Table(name = "trade_sessions", indexes = {
    @Index(name = "idx_trade_sessions_initiator", columnList = "initiator_character_id"),
    @Index(name = "idx_trade_sessions_receiver", columnList = "receiver_character_id"),
    @Index(name = "idx_trade_sessions_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradeSessionEntity {

    @Id
    @Column(name = "session_id", length = 64)
    private String sessionId;

    @Column(name = "initiator_character_id", length = 64, nullable = false)
    private String initiatorCharacterId;

    @Column(name = "receiver_character_id", length = 64, nullable = false)
    private String receiverCharacterId;

    @Column(name = "status", length = 20, nullable = false)
    private String status; // pending, accepted, declined, active, confirmed_initiator, confirmed_receiver, completed, cancelled

    @Column(name = "initiator_offer_items_json", columnDefinition = "TEXT")
    private String initiatorOfferItemsJson; // JSON массив items

    @Column(name = "initiator_offer_gold")
    private Long initiatorOfferGold;

    @Column(name = "receiver_offer_items_json", columnDefinition = "TEXT")
    private String receiverOfferItemsJson;

    @Column(name = "receiver_offer_gold")
    private Long receiverOfferGold;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;
}

