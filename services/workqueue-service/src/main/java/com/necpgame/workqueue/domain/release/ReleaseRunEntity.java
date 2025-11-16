package com.necpgame.workqueue.domain.release;

import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "release_runs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseRunEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @Column(name = "change_id", nullable = false, length = 128, unique = true)
    private String changeId;

    @Column(nullable = false, length = 256)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_agent_id")
    private AgentEntity author;

    @Column(name = "release_date", nullable = false)
    private OffsetDateTime releaseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "impact_level_id")
    private EnumValueEntity impactLevel;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "scope_description_json", columnDefinition = "JSONB", nullable = false)
    private String scopeDescriptionJson;

    @Column(name = "rollback_plan", columnDefinition = "TEXT")
    private String rollbackPlan;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private EnumValueEntity status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "releaseRun")
    @jakarta.persistence.OrderBy("sortOrder ASC")
    @Builder.Default
    private List<ReleaseRunStepEntity> steps = new ArrayList<>();

    @OneToMany(mappedBy = "releaseRun")
    @Builder.Default
    private List<ReleaseRunValidationEntity> validations = new ArrayList<>();
}


