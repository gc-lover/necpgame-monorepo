package com.necpgame.workqueue.web.dto.qa;

import com.necpgame.workqueue.web.dto.content.ContentReferenceDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record QaPlanDto(
        UUID id,
        String planCode,
        String featureName,
        ContentReferenceDto content,
        UUID preparedById,
        String preparedByName,
        OffsetDateTime planDate,
        Object scopeIn,
        Object scopeOut,
        Object environments,
        Object testTypes,
        Object testCasesSummary,
        Object entryCriteria,
        Object exitCriteria,
        Object risks,
        OffsetDateTime scheduleStartDate,
        OffsetDateTime scheduleEndDate,
        Object approvals,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        List<QaPlanItemDto> items,
        QaReportDto report
) {
}


