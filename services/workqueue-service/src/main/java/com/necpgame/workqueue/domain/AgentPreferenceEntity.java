package com.necpgame.workqueue.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "agent_preferences")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentPreferenceEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(name = "agent_id", nullable = false, unique = true)
    private UUID agentId;

    @Column(name = "role_key", nullable = false, length = 64)
    private String roleKey;

    @Column(name = "primary_segments", length = 512)
    private String primarySegments;

    @Column(name = "fallback_segments", length = 512)
    private String fallbackSegments;

    @Column(name = "pickup_statuses", length = 512)
    private String pickupStatuses;

    @Column(name = "active_statuses", length = 512)
    private String activeStatuses;

    @Column(name = "accept_status", nullable = false, length = 64)
    private String acceptStatus;

    @Column(name = "return_status", nullable = false, length = 64)
    private String returnStatus;

    @Column(name = "max_in_progress_minutes", nullable = false)
    private Integer maxInProgressMinutes;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}

