package com.necpgame.workqueue.web.dto.world;

import com.necpgame.workqueue.web.dto.content.ContentSummaryDto;
import com.necpgame.workqueue.web.dto.content.EnumValueDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record WorldLocationDetailDto(
        ContentSummaryDto summary,
        EnumValueDto region,
        EnumValueDto biome,
        UUID parentLocationId,
        Integer dangerLevel,
        Integer recommendedLevelMin,
        Integer recommendedLevelMax,
        Integer populationEstimate,
        Map<String, Object> coordinates,
        Map<String, Object> metadata,
        List<LinkDto> links,
        List<SpawnPointDto> spawnPoints
) {
    public record LinkDto(
            UUID id,
            UUID toLocationId,
            String linkType,
            Integer travelTimeMinutes,
            Map<String, Object> metadata
    ) {
    }

    public record SpawnPointDto(
            UUID id,
            String spawnType,
            UUID targetEntityId,
            Integer respawnSeconds,
            Map<String, Object> conditions,
            Map<String, Object> metadata
    ) {
    }
}

