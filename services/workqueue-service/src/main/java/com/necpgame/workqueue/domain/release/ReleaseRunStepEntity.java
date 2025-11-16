package com.necpgame.workqueue.domain.release;

import com.necpgame.workqueue.domain.reference.EnumValueEntity;
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

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "release_run_steps")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseRunStepEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "release_run_id", nullable = false)
    private ReleaseRunEntity releaseRun;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(name = "owner_role", length = 64)
    private String ownerRole;

    @Column(name = "action_description", columnDefinition = "TEXT", nullable = false)
    private String actionDescription;

    @Column(name = "due_date")
    private OffsetDateTime dueDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private EnumValueEntity status;

    @Column(name = "completed_at")
    private OffsetDateTime completedAt;

    @Column(name = "metadata_json", columnDefinition = "JSONB", nullable = false)
    private String metadataJson;
}


