package com.necpgame.workqueue.web.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.domain.qa.QaPlanEntity;
import com.necpgame.workqueue.domain.qa.QaPlanItemEntity;
import com.necpgame.workqueue.domain.qa.QaReportEntity;
import com.necpgame.workqueue.web.dto.content.ContentReferenceDto;
import com.necpgame.workqueue.web.dto.content.EnumValueDto;
import com.necpgame.workqueue.web.dto.qa.QaPlanDto;
import com.necpgame.workqueue.web.dto.qa.QaPlanItemDto;
import com.necpgame.workqueue.web.dto.qa.QaPlanListResponseDto;
import com.necpgame.workqueue.web.dto.qa.QaReportDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class QaMapper {
    private final ObjectMapper objectMapper;
    private final ContentMapper contentMapper;

    public QaPlanListResponseDto toListResponse(List<QaPlanEntity> plans) {
        List<QaPlanDto> dto = plans.stream().map(this::toDto).collect(Collectors.toList());
        return new QaPlanListResponseDto(dto);
    }

    public QaPlanDto toDto(QaPlanEntity plan) {
        List<QaPlanItemDto> items = plan.getItems().stream()
                .map(this::toItem)
                .collect(Collectors.toList());
        QaReportDto reportDto = plan.getReport() != null ? toReport(plan.getReport()) : null;
        return new QaPlanDto(
                plan.getId(),
                plan.getPlanCode(),
                plan.getFeatureName(),
                toContent(plan),
                plan.getPreparedBy() != null ? plan.getPreparedBy().getId() : null,
                plan.getPreparedBy() != null ? plan.getPreparedBy().getDisplayName() : null,
                plan.getPlanDate(),
                parse(plan.getScopeInJson()),
                parse(plan.getScopeOutJson()),
                parse(plan.getEnvironmentsJson()),
                parse(plan.getTestTypesJson()),
                parse(plan.getTestCasesSummaryJson()),
                parse(plan.getEntryCriteriaJson()),
                parse(plan.getExitCriteriaJson()),
                parse(plan.getRisksJson()),
                plan.getScheduleStartDate(),
                plan.getScheduleEndDate(),
                parse(plan.getApprovalsJson()),
                plan.getCreatedAt(),
                plan.getUpdatedAt(),
                items,
                reportDto
        );
    }

    private QaPlanItemDto toItem(QaPlanItemEntity item) {
        return new QaPlanItemDto(
                item.getId(),
                item.getSortOrder(),
                item.getDescription(),
                item.getExpectedResult(),
                toEnum(item.getTestType()),
                item.getAutomationStatus(),
                parse(item.getMetadataJson())
        );
    }

    private QaReportDto toReport(QaReportEntity report) {
        return new QaReportDto(
                report.getId(),
                report.getTester() != null ? report.getTester().getId() : null,
                report.getTester() != null ? report.getTester().getDisplayName() : null,
                report.getReportDate(),
                report.getSummary(),
                parse(report.getExecutionMetricsJson()),
                parse(report.getDefectsReferenceJson()),
                parse(report.getRisksMitigationsJson()),
                toEnum(report.getReleaseDecision()),
                parse(report.getRecommendationsJson()),
                parse(report.getApprovalsJson()),
                report.getCreatedAt(),
                report.getUpdatedAt()
        );
    }

    private ContentReferenceDto toContent(QaPlanEntity plan) {
        if (plan.getContentEntity() == null) {
            return null;
        }
        return new ContentReferenceDto(
                plan.getContentEntity().getId(),
                plan.getContentEntity().getCode(),
                plan.getContentEntity().getTitle()
        );
    }

    private EnumValueDto toEnum(com.necpgame.workqueue.domain.reference.EnumValueEntity value) {
        return contentMapper.toEnum(value);
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


