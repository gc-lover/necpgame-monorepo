package com.necpgame.workqueue.web.dto.process;

import java.util.UUID;

public record ChecklistItemDto(
        UUID id,
        Integer sortOrder,
        String description,
        String expectedResult,
        Object metadata
) {
}


