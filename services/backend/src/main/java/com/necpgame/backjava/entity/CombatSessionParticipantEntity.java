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
import jakarta.persistence.UniqueConstraint;
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
@Table(name = "combat_session_participants", indexes = {
    @Index(name = "idx_combat_session_participants_session", columnList = "session_id"),
    @Index(name = "idx_combat_session_participants_status", columnList = "status")
}, uniqueConstraints = {
    @UniqueConstraint(name = "uk_combat_session_participant_reference", columnNames = {"session_id", "reference_id"})
})
public class CombatSessionParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "session_id", nullable = false)
    private UUID sessionId;

    @Enumerated(EnumType.STRING)
    @Column(name = "participant_type", nullable = false, length = 20)
    private ParticipantType participantType;

    @Column(name = "reference_id", nullable = false, length = 100)
    private String referenceId;

    @Column(name = "team", length = 20)
    private String team;

    @Column(name = "character_name", length = 150)
    private String characterName;

    @Column(name = "hp", nullable = false)
    private Integer hp;

    @Column(name = "max_hp", nullable = false)
    private Integer maxHp;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ParticipantStatus status;

    @Column(name = "status_effects", columnDefinition = "jsonb")
    private String statusEffectsJson;

    @Column(name = "position", columnDefinition = "jsonb")
    private String positionJson;

    @Column(name = "damage_dealt")
    private Integer damageDealt;

    @Column(name = "damage_taken")
    private Integer damageTaken;

    @Column(name = "kills")
    private Integer kills;

    @Column(name = "deaths")
    private Integer deaths;

    @Column(name = "headshots")
    private Integer headshots;

    @Column(name = "abilities_used")
    private Integer abilitiesUsed;

    @Column(name = "order_index")
    private Integer orderIndex;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public enum ParticipantType {
        PLAYER,
        NPC,
        AI_ENEMY
    }

    public enum ParticipantStatus {
        ALIVE,
        DOWNED,
        DEAD
    }
}



