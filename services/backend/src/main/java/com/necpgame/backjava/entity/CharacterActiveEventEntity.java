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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * CharacterActiveEventEntity - активные события персонажа.
 */
@Entity
@Table(name = "character_active_events", indexes = {
    @Index(name = "idx_character_active_events_character", columnList = "character_id"),
    @Index(name = "idx_character_active_events_event", columnList = "event_id"),
    @Index(name = "idx_character_active_events_status", columnList = "status")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CharacterActiveEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Column(name = "event_id", nullable = false, length = 100)
    private String eventId;

    @Builder.Default
    @Column(name = "status", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private EventStatus status = EventStatus.ACTIVE;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "triggered_at", nullable = false)
    private LocalDateTime triggeredAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "location_id", length = 120)
    private String locationId;

    @Column(name = "location_type", length = 40)
    private String locationType;

    @Column(name = "time_of_day", length = 20)
    private String timeOfDay;

    @Column(name = "generation_chance", precision = 6, scale = 4)
    private Double generationChance;

    @Column(name = "event_snapshot", columnDefinition = "jsonb")
    private String eventSnapshot;

    @Column(name = "choice_id", length = 100)
    private String choiceId;

    @Column(name = "outcome_id", length = 100)
    private String outcomeId;

    @Column(name = "consequences_snapshot", columnDefinition = "jsonb")
    private String consequencesSnapshot;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CharacterEntity character;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", referencedColumnName = "id", insertable = false, updatable = false)
    private RandomEventEntity event;

    public enum EventStatus {
        ACTIVE,
        COMPLETED,
        EXPIRED,
        IGNORED
    }
}

