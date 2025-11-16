package com.necpgame.workqueue.web.dto.npc;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record NpcCommandRequestDto(
        @NotBlank String contentCode,
        String alignmentCode,
        String behaviorCode,
        UUID factionEntityId,
        String roleTitle,
        Integer level,
        BigDecimal powerScore,
        Map<String, Object> vendorCatalog,
        Map<String, Object> scheduleMetadata,
        Map<String, Object> dialogueProfile,
        @NotNull Map<String, Object> metadata,
        @Valid List<ScheduleEntryDto> schedule,
        @Valid List<InventoryItemDto> inventory,
        @Valid List<DialogueLinkDto> dialogueLinks
) {
    public record ScheduleEntryDto(
            String dayTimeRange,
            UUID locationEntityId,
            @NotNull Map<String, Object> payload
    ) {
    }

    public record InventoryItemDto(
            @NotNull UUID itemEntityId,
            @NotNull @Min(1) Integer quantity,
            Integer restockIntervalMinutes,
            BigDecimal priceOverride,
            @NotNull Map<String, Object> metadata
    ) {
    }

    public record DialogueLinkDto(
            @NotNull UUID dialogueEntityId,
            Integer priority,
            Map<String, Object> conditions,
            Map<String, Object> metadata
    ) {
    }
}

