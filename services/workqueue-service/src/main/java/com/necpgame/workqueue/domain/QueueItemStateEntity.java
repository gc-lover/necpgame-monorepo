package com.necpgame.workqueue.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "queue_item_states")
public class QueueItemStateEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    private QueueItemEntity item;

    @Column(name = "status_code", nullable = false, length = 64)
    private String statusCode;

    @Column(columnDefinition = "TEXT")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "actor_id")
    private AgentEntity actor;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Column(name = "status_value_id")
    private UUID statusValueId;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @NonNull
    public static QueueItemStateEntity create(QueueItemEntity item, AgentEntity actor, String statusCode, UUID statusValueId, String note, String metadata, OffsetDateTime createdAt) {
        QueueItemStateEntity state = new QueueItemStateEntity();
        state.id = UUID.randomUUID();
        state.item = item;
        state.statusCode = statusCode;
        state.note = note;
        state.actor = actor;
        state.metadata = metadata;
        state.statusValueId = statusValueId;
        state.createdAt = createdAt;
        return state;
    }
}


