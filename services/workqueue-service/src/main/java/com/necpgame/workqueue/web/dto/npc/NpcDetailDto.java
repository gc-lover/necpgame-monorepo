package com.necpgame.workqueue.web.dto.npc;

import com.necpgame.workqueue.web.dto.content.ContentSummaryDto;
import com.necpgame.workqueue.web.dto.content.EnumValueDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record NpcDetailDto(
        ContentSummaryDto summary,
        EnumValueDto alignment,
        EnumValueDto behavior,
        UUID factionId,
        String roleTitle,
        Integer level,
        BigDecimal powerScore,
        Map<String, Object> vendorCatalog,
        Map<String, Object> scheduleMetadata,
        Map<String, Object> dialogueProfile,
        Map<String, Object> metadata,
        List<ScheduleEntryDto> schedule,
        List<InventoryItemDto> inventory,
        List<DialogueLinkDto> dialogueLinks
) {
    public record ScheduleEntryDto(
            UUID id,
            String dayTimeRange,
            UUID locationId,
            Map<String, Object> payload
    ) {
    }

    public record InventoryItemDto(
            UUID id,
            UUID itemId,
            Integer quantity,
            Integer restockIntervalMinutes,
            BigDecimal priceOverride,
            Map<String, Object> metadata
    ) {
    }

    public record DialogueLinkDto(
            UUID id,
            UUID dialogueId,
            Integer priority,
            Map<String, Object> conditions,
            Map<String, Object> metadata
    ) {
    }
}

