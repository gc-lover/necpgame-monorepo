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
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "quest_dialogue_state", indexes = {
    @Index(name = "idx_dialogue_state_instance", columnList = "quest_instance_id", unique = true)
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestDialogueStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "quest_instance_id", nullable = false)
    private UUID questInstanceId;

    @Column(name = "current_node_id", length = 100, nullable = false)
    private String currentNodeId;

    @Column(name = "visited_nodes", columnDefinition = "jsonb")
    private String visitedNodesJson;

    @Column(name = "choices_made", columnDefinition = "jsonb")
    private String choicesMadeJson;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}



