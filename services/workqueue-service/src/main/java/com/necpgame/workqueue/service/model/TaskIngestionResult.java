package com.necpgame.workqueue.service.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TaskIngestionResult(
        UUID itemId,
        UUID queueId,
        String segment,
        String status,
        OffsetDateTime createdAt
) {
}

