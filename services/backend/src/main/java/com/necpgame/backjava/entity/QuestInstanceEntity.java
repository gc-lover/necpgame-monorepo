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
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "quest_instances", indexes = {
    @Index(name = "idx_quest_instances_character", columnList = "character_id, status"),
    @Index(name = "idx_quest_instances_template", columnList = "quest_template_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestInstanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "character_id", nullable = false)
    private UUID characterId;

    @Column(name = "quest_template_id", nullable = false, length = 100)
    private String questTemplateId;

    @Column(name = "quest_name", length = 200)
    private String questName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private QuestStatus status;

    @Column(name = "current_branch_id", length = 100)
    private String currentBranchId;

    @Column(name = "current_dialogue_node_id", length = 100)
    private String currentDialogueNodeId;

    @Column(name = "progress_json", columnDefinition = "jsonb")
    private String progressJson;

    @Column(name = "flags_json", columnDefinition = "jsonb")
    private String flagsJson;

    @Column(name = "started_at", nullable = false)
    private OffsetDateTime startedAt;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public enum QuestStatus {
        ACTIVE,
        COMPLETED,
        FAILED,
        ABANDONED
    }
}



