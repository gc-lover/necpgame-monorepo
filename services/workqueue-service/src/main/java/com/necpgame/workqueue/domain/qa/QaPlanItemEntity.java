package com.necpgame.workqueue.domain.qa;

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

import java.util.UUID;

@Entity
@Table(name = "qa_plan_items")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QaPlanItemEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "qa_plan_id", nullable = false)
    private QaPlanEntity plan;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "expected_result", columnDefinition = "TEXT")
    private String expectedResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_type_id")
    private EnumValueEntity testType;

    @Column(name = "automation_status", length = 64)
    private String automationStatus;

    @Column(name = "metadata_json", columnDefinition = "JSONB", nullable = false)
    private String metadataJson;
}


