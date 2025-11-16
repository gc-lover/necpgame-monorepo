package com.necpgame.workqueue.web.dto.world;

import com.necpgame.workqueue.web.dto.content.ContentSummaryDto;
import com.necpgame.workqueue.web.dto.content.EnumValueDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record WorldEventDetailDto(
        ContentSummaryDto summary,
        EnumValueDto eventType,
        EnumValueDto region,
        UUID locationId,
        Integer difficultyTier,
        Map<String, Object> recurrencePattern,
        UUID rewardEntityId,
        String rewardDescription,
        Map<String, Object> metadata,
        List<RequirementDto> requirements
) {
    public record RequirementDto(
            UUID id,
            Map<String, Object> payload
    ) {
    }
}

