package com.necpgame.workqueue.web.dto.analytics;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record AnalyticsMetricDto(
        UUID id,
        String metricCode,
        String displayName,
        String description,
        BigDecimal targetValue,
        BigDecimal currentValue,
        OffsetDateTime lastUpdated,
        Object metadata
) {
}


