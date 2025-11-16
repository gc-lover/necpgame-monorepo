package com.necpgame.workqueue.domain.qa;

import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "qa_reports")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QaReportEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "qa_plan_id", nullable = false, unique = true)
    private QaPlanEntity plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tester_agent_id")
    private AgentEntity tester;

    @Column(name = "report_date", nullable = false)
    private OffsetDateTime reportDate;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(name = "execution_metrics_json", columnDefinition = "JSONB", nullable = false)
    private String executionMetricsJson;

    @Column(name = "defects_reference_json", columnDefinition = "JSONB", nullable = false)
    private String defectsReferenceJson;

    @Column(name = "risks_mitigations_json", columnDefinition = "JSONB", nullable = false)
    private String risksMitigationsJson;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "release_decision_id")
    private EnumValueEntity releaseDecision;

    @Column(name = "recommendations_json", columnDefinition = "JSONB", nullable = false)
    private String recommendationsJson;

    @Column(name = "approvals_json", columnDefinition = "JSONB", nullable = false)
    private String approvalsJson;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}


