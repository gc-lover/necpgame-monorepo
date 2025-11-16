package com.necpgame.workqueue.web.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.domain.analytics.AnalyticsMetricEntity;
import com.necpgame.workqueue.domain.analytics.AnalyticsSchemaEntity;
import com.necpgame.workqueue.web.dto.analytics.AnalyticsMetricDto;
import com.necpgame.workqueue.web.dto.analytics.AnalyticsSchemaDto;
import com.necpgame.workqueue.web.dto.analytics.AnalyticsSchemaListResponseDto;
import com.necpgame.workqueue.web.dto.content.ContentReferenceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AnalyticsMapper {
    private final ObjectMapper objectMapper;

    public AnalyticsSchemaListResponseDto toListResponse(List<AnalyticsSchemaEntity> schemas) {
        List<AnalyticsSchemaDto> dto = schemas.stream().map(this::toDto).collect(Collectors.toList());
        return new AnalyticsSchemaListResponseDto(dto);
    }

    public AnalyticsSchemaDto toDto(AnalyticsSchemaEntity schema) {
        List<AnalyticsMetricDto> metrics = schema.getMetrics().stream()
                .map(this::toMetric)
                .collect(Collectors.toList());
        return new AnalyticsSchemaDto(
                schema.getId(),
                toContent(schema),
                schema.getFeatureName(),
                parse(schema.getKpiJson()),
                parse(schema.getEventsSchemaJson()),
                parse(schema.getDashboardsLinksJson()),
                schema.getLastValidatedAt(),
                parse(schema.getValidationResultsJson()),
                schema.getCreatedAt(),
                schema.getUpdatedAt(),
                metrics
        );
    }

    private AnalyticsMetricDto toMetric(AnalyticsMetricEntity metric) {
        return new AnalyticsMetricDto(
                metric.getId(),
                metric.getMetricCode(),
                metric.getDisplayName(),
                metric.getDescription(),
                metric.getTargetValue(),
                metric.getCurrentValue(),
                metric.getLastUpdated(),
                parse(metric.getMetadataJson())
        );
    }

    private ContentReferenceDto toContent(AnalyticsSchemaEntity schema) {
        if (schema.getContentEntity() == null) {
            return null;
        }
        return new ContentReferenceDto(
                schema.getContentEntity().getId(),
                schema.getContentEntity().getCode(),
                schema.getContentEntity().getTitle()
        );
    }

    private Object parse(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            Object parsed = objectMapper.readValue(json, Object.class);
            if (parsed instanceof Map<?, ?> map) {
                Map<String, Object> ordered = new LinkedHashMap<>();
                map.forEach((key, value) -> {
                    if (key instanceof String stringKey) {
                        ordered.put(stringKey, value);
                    }
                });
                return ordered;
            }
            return parsed;
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}


