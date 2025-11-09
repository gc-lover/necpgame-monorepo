package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name = "quest_skill_check_results", indexes = {
    @Index(name = "idx_skill_check_instance", columnList = "quest_instance_id"),
    @Index(name = "idx_skill_check_node", columnList = "dialogue_node_id")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestSkillCheckResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "quest_instance_id", nullable = false)
    private UUID questInstanceId;

    @Column(name = "dialogue_node_id", length = 100, nullable = false)
    private String dialogueNodeId;

    @Column(name = "skill_name", length = 50, nullable = false)
    private String skillName;

    @Column(name = "difficulty_class", nullable = false)
    private Integer difficultyClass;

    @Column(name = "dice_roll", nullable = false)
    private Integer diceRoll;

    @Column(name = "secondary_roll")
    private Integer secondaryRoll;

    @Column(name = "skill_modifier")
    private Integer skillModifier;

    @Column(name = "total_result", nullable = false)
    private Integer totalResult;

    @Column(name = "success", nullable = false)
    private boolean success;

    @Column(name = "critical_success")
    private boolean criticalSuccess;

    @Column(name = "critical_failure")
    private boolean criticalFailure;

    @Column(name = "advantage_used")
    private boolean advantageUsed;

    @CreationTimestamp
    @Column(name = "rolled_at", nullable = false, updatable = false)
    private OffsetDateTime rolledAt;
}



