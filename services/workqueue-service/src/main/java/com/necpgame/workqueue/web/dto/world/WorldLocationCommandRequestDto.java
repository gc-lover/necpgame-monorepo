package com.necpgame.workqueue.web.dto.world;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record WorldLocationCommandRequestDto(
        @NotBlank String contentCode,
        String regionCode,
        String biomeCode,
        UUID parentLocationId,
        Integer dangerLevel,
        Integer recommendedLevelMin,
        Integer recommendedLevelMax,
        Integer populationEstimate,
        Map<String, Object> coordinates,
        @NotNull Map<String, Object> metadata,
        @Valid List<LinkDto> links,
        @Valid List<SpawnPointDto> spawnPoints
) {
    public record LinkDto(
            @NotNull UUID toLocationId,
            String linkType,
            Integer travelTimeMinutes,
            Map<String, Object> metadata
    ) {
    }

    public record SpawnPointDto(
            String spawnType,
            UUID targetEntityId,
            Integer respawnSeconds,
            Map<String, Object> conditions,
            Map<String, Object> metadata
    ) {
    }
}

