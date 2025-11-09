package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "reset_history", indexes = {
    @Index(name = "idx_reset_history_type_time", columnList = "reset_type, execution_time DESC")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetHistoryEntity {

    @Id
    @Column(name = "id", nullable = false, columnDefinition = "UUID")
    private UUID id;

    @Column(name = "reset_type", length = 20, nullable = false)
    private String resetType;

    @Column(name = "execution_time", nullable = false)
    private OffsetDateTime executionTime;

    @Column(name = "triggered_by", length = 20, nullable = false)
    private String triggeredBy;

    @Column(name = "affected_players")
    private Integer affectedPlayers;

    @Column(name = "success")
    private Boolean success;

    @Column(name = "execution_duration_ms")
    private Integer executionDurationMs;

    @Column(name = "items_reset", columnDefinition = "JSONB")
    private String itemsReset;

    @Column(name = "errors", columnDefinition = "JSONB")
    private String errors;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}

