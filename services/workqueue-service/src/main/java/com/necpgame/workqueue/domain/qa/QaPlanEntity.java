package com.necpgame.workqueue.domain.qa;

import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "qa_plans")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QaPlanEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "content_entity_id", nullable = false, unique = true)
    private ContentEntryEntity contentEntity;

    @Column(name = "plan_code", nullable = false, length = 128)
    private String planCode;

    @Column(name = "feature_name", nullable = false, length = 256)
    private String featureName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prepared_by_agent_id")
    private AgentEntity preparedBy;

    @Column(name = "plan_date", nullable = false)
    private OffsetDateTime planDate;

    @Column(name = "scope_in_json", columnDefinition = "JSONB", nullable = false)
    private String scopeInJson;

    @Column(name = "scope_out_json", columnDefinition = "JSONB", nullable = false)
    private String scopeOutJson;

    @Column(name = "environments_json", columnDefinition = "JSONB", nullable = false)
    private String environmentsJson;

    @Column(name = "test_types_json", columnDefinition = "JSONB", nullable = false)
    private String testTypesJson;

    @Column(name = "test_cases_summary_json", columnDefinition = "JSONB", nullable = false)
    private String testCasesSummaryJson;

    @Column(name = "entry_criteria_json", columnDefinition = "JSONB", nullable = false)
    private String entryCriteriaJson;

    @Column(name = "exit_criteria_json", columnDefinition = "JSONB", nullable = false)
    private String exitCriteriaJson;

    @Column(name = "risks_json", columnDefinition = "JSONB", nullable = false)
    private String risksJson;

    @Column(name = "schedule_start_date")
    private OffsetDateTime scheduleStartDate;

    @Column(name = "schedule_end_date")
    private OffsetDateTime scheduleEndDate;

    @Column(name = "approvals_json", columnDefinition = "JSONB", nullable = false)
    private String approvalsJson;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "plan")
    @jakarta.persistence.OrderBy("sortOrder ASC")
    @Builder.Default
    private List<QaPlanItemEntity> items = new ArrayList<>();

    @OneToOne(mappedBy = "plan")
    private QaReportEntity report;
}


