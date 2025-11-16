package com.necpgame.workqueue.web.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record QueueItemSummaryDto(
        UUID id,
        UUID queueId,
        String title,
        UUID statusValueId,
        String statusCode,
        int priority,
        UUID assignedTo,
        OffsetDateTime dueAt,
        OffsetDateTime updatedAt,
        OffsetDateTime lockedUntil,
        long version
) {
}

