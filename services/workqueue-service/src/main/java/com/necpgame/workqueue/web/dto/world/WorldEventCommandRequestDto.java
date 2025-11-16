package com.necpgame.workqueue.web.dto.world;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record WorldEventCommandRequestDto(
        @NotBlank String contentCode,
        String eventTypeCode,
        String regionCode,
        UUID locationEntityId,
        Integer difficultyTier,
        Map<String, Object> recurrencePattern,
        UUID rewardEntityId,
        String rewardDescription,
        @NotNull Map<String, Object> metadata,
        @Valid List<RequirementDto> requirements
) {
    public record RequirementDto(
            @NotNull Map<String, Object> payload
    ) {
    }
}

