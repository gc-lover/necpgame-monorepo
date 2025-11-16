package com.necpgame.workqueue.web.dto.content;

import java.util.UUID;

public record ContentLocalizationDto(
        UUID id,
        String locale,
        String title,
        String description,
        String flavorText,
        Object metadata
) {
}


