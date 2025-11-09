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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * CharacterEventHistoryEntity - история случайных событий персонажа.
 */
@Entity
@Table(name = "character_event_history", indexes = {
    @Index(name = "idx_character_event_history_character", columnList = "character_id"),
    @Index(name = "idx_character_event_history_event", columnList = "event_id"),
    @Index(name = "idx_character_event_history_resolved", columnList = "resolved_at")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CharacterEventHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Column(name = "event_id", nullable = false, length = 100)
    private String eventId;

    @Column(name = "event_name", length = 200)
    private String eventName;

    @Column(name = "period", length = 20)
    private String period;

    @Column(name = "instance_id")
    private UUID instanceId;

    @Column(name = "choice_id", length = 100)
    private String choiceId;

    @Column(name = "outcome_id", length = 100)
    private String outcomeId;

    @Column(name = "triggered_at")
    private LocalDateTime triggeredAt;

    @Column(name = "resolved_at", nullable = false)
    private LocalDateTime resolvedAt;

    @Column(name = "consequences_summary", columnDefinition = "TEXT")
    private String consequencesSummary;

    @Column(name = "event_snapshot", columnDefinition = "jsonb")
    private String eventSnapshot;

    @Column(name = "outcome_snapshot", columnDefinition = "jsonb")
    private String outcomeSnapshot;

    @Column(name = "skill_check_snapshot", columnDefinition = "jsonb")
    private String skillCheckSnapshot;
}

