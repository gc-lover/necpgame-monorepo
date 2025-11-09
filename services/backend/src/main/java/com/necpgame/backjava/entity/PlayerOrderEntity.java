package com.necpgame.backjava.entity;

import com.necpgame.backjava.entity.enums.PlayerOrderDifficulty;
import com.necpgame.backjava.entity.enums.PlayerOrderStatus;
import com.necpgame.backjava.entity.enums.PlayerOrderType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "player_orders", indexes = {
    @Index(name = "idx_player_orders_status", columnList = "status"),
    @Index(name = "idx_player_orders_type", columnList = "order_type"),
    @Index(name = "idx_player_orders_creator", columnList = "creator_id"),
    @Index(name = "idx_player_orders_executor", columnList = "executor_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerOrderEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "creator_id", nullable = false)
    private UUID creatorId;

    @Column(name = "creator_name", length = 160)
    private String creatorName;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false, length = 40)
    private PlayerOrderType type;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "requirements_json", columnDefinition = "jsonb")
    private String requirementsJson;

    @Column(name = "payment", nullable = false)
    private Integer payment;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 40)
    private PlayerOrderStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty", length = 40)
    private PlayerOrderDifficulty difficulty;

    @Column(name = "executor_id")
    private UUID executorId;

    @Column(name = "executor_name", length = 160)
    private String executorName;

    @Column(name = "npc_executor_id")
    private UUID npcExecutorId;

    @Builder.Default
    @Column(name = "views", nullable = false)
    private Integer views = 0;

    @Column(name = "deadline")
    private Instant deadline;

    @Column(name = "accepted_at")
    private Instant acceptedAt;

    @Column(name = "estimated_completion")
    private Instant estimatedCompletion;

    @Column(name = "completed_at")
    private Instant completedAt;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Column(name = "deliverables_json", columnDefinition = "jsonb")
    private String deliverablesJson;

    @Column(name = "escrow_json", columnDefinition = "jsonb")
    private String escrowJson;

    @Column(name = "completion_proof_json", columnDefinition = "jsonb")
    private String completionProofJson;

    @Builder.Default
    @Column(name = "recurring", nullable = false)
    private Boolean recurring = Boolean.FALSE;

    @Builder.Default
    @Column(name = "premium", nullable = false)
    private Boolean premium = Boolean.FALSE;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PlayerOrderReviewEntity> reviews = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private Instant updatedAt;
}

