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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "character_faction_quest_progress",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_character_faction_quest_progress_character_quest", columnNames = {"character_id", "quest_id"})
    },
    indexes = {
        @Index(name = "idx_character_faction_quest_progress_character", columnList = "character_id"),
        @Index(name = "idx_character_faction_quest_progress_quest", columnList = "quest_id")
    }
)
public class CharacterFactionQuestProgressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Column(name = "quest_id", length = 120, nullable = false)
    private String questId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private ProgressStatus status;

    @Column(name = "current_branch", length = 120)
    private String currentBranch;

    @Column(name = "choices_json", columnDefinition = "jsonb")
    private String choicesJson;

    @Column(name = "objectives_json", columnDefinition = "jsonb")
    private String objectivesJson;

    @Column(name = "ending_achieved", length = 120)
    private String endingAchieved;

    @Column(name = "completion_date")
    private OffsetDateTime completionDate;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public enum ProgressStatus {
        ACTIVE,
        COMPLETED,
        FAILED,
        ABANDONED
    }
}


