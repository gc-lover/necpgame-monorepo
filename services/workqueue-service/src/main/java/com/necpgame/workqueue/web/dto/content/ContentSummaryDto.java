package com.necpgame.workqueue.web.dto.content;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ContentSummaryDto(
        UUID id,
        String code,
        String title,
        EnumValueDto entityType,
        EnumValueDto status,
        EnumValueDto category,
        EnumValueDto visibility,
        EnumValueDto riskLevel,
        String ownerRole,
        String version,
        OffsetDateTime lastUpdated,
        OffsetDateTime createdAt
) {
}


