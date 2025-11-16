package com.necpgame.workqueue.web.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record QueueItemStateDto(
        UUID id,
        UUID statusValueId,
        String statusCode,
        String note,
        UUID actorId,
        String metadata,
        OffsetDateTime createdAt
) {
}


