package com.necpgame.workqueue.domain.release;

import com.necpgame.workqueue.domain.AgentEntity;
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
@Table(name = "release_run_validations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseRunValidationEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "release_run_id", nullable = false)
    private ReleaseRunEntity releaseRun;

    @Column(name = "validation_type", nullable = false, length = 128)
    private String validationType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private EnumValueEntity status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "validated_by_agent_id")
    private AgentEntity validatedBy;

    @Column(name = "validated_at")
    private OffsetDateTime validatedAt;

    @Column(name = "results_json", columnDefinition = "JSONB", nullable = false)
    private String resultsJson;
}


