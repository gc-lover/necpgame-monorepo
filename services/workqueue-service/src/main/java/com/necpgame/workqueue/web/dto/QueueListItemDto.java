package com.necpgame.workqueue.web.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record QueueListItemDto(
        UUID id,
        String segment,
        String statusCode,
        String title,
        OffsetDateTime updatedAt
) {
}


