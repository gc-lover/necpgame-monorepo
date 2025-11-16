package com.necpgame.workqueue.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "queue_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueItemEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "queue_id", nullable = false)
    private QueueEntity queue;

    @Column(name = "external_ref", length = 512)
    private String externalRef;

    @Column(nullable = false, length = 256)
    private String title;

    @Column(nullable = false)
    private int priority;

    @Column(columnDefinition = "TEXT")
    private String payload;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private AgentEntity createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private AgentEntity assignedTo;

    @Column(name = "due_at")
    private OffsetDateTime dueAt;

    @Column(name = "locked_until")
    private OffsetDateTime lockedUntil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_state_id")
    private QueueItemStateEntity currentState;

    @Column(name = "status_value_id")
    private UUID statusValueId;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Version
    @Column(nullable = false)
    private Long version;

    public void assignTo(AgentEntity assignee) {
        this.assignedTo = assignee;
    }

    public void updatePayload(String payload) {
        this.payload = payload;
    }

    public void clearLock() {
        this.lockedUntil = null;
    }

    public void updateTimestamp(OffsetDateTime value) {
        this.updatedAt = value;
    }

    public void applyState(QueueItemStateEntity state) {
        QueueItemStateEntity resolved = Objects.requireNonNull(state);
        this.currentState = resolved;
        this.statusValueId = resolved.getStatusValueId();
    }
}

