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
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "combat_session_instances", indexes = {
    @Index(name = "idx_combat_session_instances_status", columnList = "status"),
    @Index(name = "idx_combat_session_instances_started_at", columnList = "started_at")
})
public class CombatSessionInstanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "combat_type", nullable = false, length = 30)
    private CombatType combatType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private CombatStatus status;

    @Column(name = "turn_based", nullable = false)
    private boolean turnBased;

    @Column(name = "time_limit_seconds")
    private Integer timeLimitSeconds;

    @Column(name = "location_id", length = 100)
    private String locationId;

    @Column(name = "instance_id", length = 100)
    private String instanceId;

    @Column(name = "turn_order", columnDefinition = "text")
    private String turnOrder;

    @Column(name = "current_turn_index")
    private Integer currentTurnIndex;

    @Column(name = "active_participant_id", length = 100)
    private String activeParticipantId;

    @Column(name = "winner_team", length = 20)
    private String winnerTeam;

    @Column(name = "settings_json", columnDefinition = "jsonb")
    private String settingsJson;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "started_at", nullable = false)
    private OffsetDateTime startedAt;

    @Column(name = "ended_at")
    private OffsetDateTime endedAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public enum CombatType {
        PVE,
        PVP_DUEL,
        PVP_ARENA,
        RAID_BOSS,
        EXTRACTION
    }

    public enum CombatStatus {
        STARTING,
        ACTIVE,
        PAUSED,
        ENDED
    }
}



