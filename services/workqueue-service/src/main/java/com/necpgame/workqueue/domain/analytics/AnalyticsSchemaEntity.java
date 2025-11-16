package com.necpgame.workqueue.domain.analytics;

import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "analytics_schemas")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsSchemaEntity {
    @Id
    @Column(nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "content_entity_id", nullable = false, unique = true)
    private ContentEntryEntity contentEntity;

    @Column(name = "feature_name", nullable = false, length = 256)
    private String featureName;

    @Column(name = "kpi_json", columnDefinition = "JSONB", nullable = false)
    private String kpiJson;

    @Column(name = "events_schema_json", columnDefinition = "JSONB", nullable = false)
    private String eventsSchemaJson;

    @Column(name = "dashboards_links_json", columnDefinition = "JSONB", nullable = false)
    private String dashboardsLinksJson;

    @Column(name = "last_validated_at")
    private OffsetDateTime lastValidatedAt;

    @Column(name = "validation_results_json", columnDefinition = "JSONB", nullable = false)
    private String validationResultsJson;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "schema")
    @jakarta.persistence.OrderBy("metricCode ASC")
    @Builder.Default
    private List<AnalyticsMetricEntity> metrics = new ArrayList<>();
}


