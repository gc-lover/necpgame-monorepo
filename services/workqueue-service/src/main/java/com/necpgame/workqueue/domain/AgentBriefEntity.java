package com.necpgame.workqueue.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Entity
@Table(name = "agent_briefs")
@Getter
@Setter
public class AgentBriefEntity {
    @Id
    @Column(length = 64, nullable = false)
    private String segment;

    @Column(name = "role_key", length = 64, nullable = false)
    private String roleKey;

    @Column(length = 128, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mission;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String responsibilities;

    @Column(name = "submission_checklist", columnDefinition = "TEXT", nullable = false)
    private String submissionChecklist;

    @Column(name = "handoff_notes", columnDefinition = "TEXT")
    private String handoffNotes;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @PrePersist
    void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}


