package com.necpgame.backjava.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "maintenance_windows", indexes = {
    @Index(name = "idx_maintenance_windows_status_start", columnList = "status, start_at"),
    @Index(name = "idx_maintenance_windows_environment", columnList = "environment")
})
public class MaintenanceWindowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "window_type", nullable = false, length = 32)
    private String type;

    @Column(name = "environment", nullable = false, length = 32)
    private String environment;

    @Column(name = "zones", columnDefinition = "TEXT")
    private String zonesJson;

    @Column(name = "services", columnDefinition = "TEXT")
    private String servicesJson;

    @Column(name = "start_at", nullable = false)
    private OffsetDateTime startAt;

    @Column(name = "end_at")
    private OffsetDateTime endAt;

    @Column(name = "expected_duration_minutes")
    private Integer expectedDurationMinutes;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "shutdown_plan", columnDefinition = "TEXT")
    private String shutdownPlanJson;

    @Column(name = "notification_plan", columnDefinition = "TEXT")
    private String notificationPlanJson;

    @Column(name = "hooks_config", columnDefinition = "TEXT")
    private String hooksJson;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "progress_percent")
    private Double progressPercent;

    @Column(name = "affected_services", columnDefinition = "TEXT")
    private String affectedServicesJson;

    @Column(name = "player_count")
    private Integer playerCount;

    @Column(name = "session_active_sessions")
    private Integer sessionActiveSessions;

    @Column(name = "session_drained_sessions")
    private Integer sessionDrainedSessions;

    @Column(name = "session_estimated_completion")
    private OffsetDateTime sessionEstimatedCompletion;

    @Column(name = "timeline_entries", columnDefinition = "TEXT")
    private String timelineJson;

    @Column(name = "status_updated_at")
    private OffsetDateTime statusUpdatedAt;

    @Column(name = "is_emergency")
    private boolean emergency;

    @PrePersist
    void onCreate() {
        if (status == null) {
            status = "PLANNED";
        }
        if (createdBy == null) {
            createdBy = "system";
        }
        if (statusUpdatedAt == null) {
            statusUpdatedAt = OffsetDateTime.now();
        }
    }
}





