package com.necpgame.workqueue.web.dto.qa;

import com.necpgame.workqueue.web.dto.content.EnumValueDto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record QaReportDto(
        UUID id,
        UUID testerId,
        String testerName,
        OffsetDateTime reportDate,
        String summary,
        Object executionMetrics,
        Object defectsReference,
        Object risksMitigations,
        EnumValueDto releaseDecision,
        Object recommendations,
        Object approvals,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}


