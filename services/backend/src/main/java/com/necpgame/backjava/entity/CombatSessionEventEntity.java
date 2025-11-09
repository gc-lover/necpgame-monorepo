package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "combat_session_events", indexes = {
    @Index(name = "idx_combat_session_events_session", columnList = "session_id"),
    @Index(name = "idx_combat_session_events_type", columnList = "event_type")
})
public class CombatSessionEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 40)
    private EventType eventType;

    @Column(name = "actor_id", length = 100)
    private String actorId;

    @Column(name = "target_id", length = 100)
    private String targetId;

    @Column(name = "payload", columnDefinition = "jsonb")
    private String payload;

    @CreationTimestamp
    @Column(name = "occurred_at", nullable = false, updatable = false)
    private OffsetDateTime occurredAt;

    public enum EventType {
        DAMAGE,
        HEAL,
        KILL,
        ABILITY_USED,
        STATUS_EFFECT,
        TURN_START,
        TURN_END,
        SESSION_CREATED,
        SESSION_ENDED
    }
}



