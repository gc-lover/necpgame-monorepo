package com.necpgame.workqueue.web.dto.analytics;

import com.necpgame.workqueue.web.dto.content.ContentReferenceDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record AnalyticsSchemaDto(
        UUID id,
        ContentReferenceDto content,
        String featureName,
        Object kpi,
        Object eventsSchema,
        Object dashboardsLinks,
        OffsetDateTime lastValidatedAt,
        Object validationResults,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        List<AnalyticsMetricDto> metrics
) {
}


