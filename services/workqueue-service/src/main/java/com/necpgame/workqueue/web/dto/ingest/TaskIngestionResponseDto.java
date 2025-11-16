package com.necpgame.workqueue.web.dto.ingest;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TaskIngestionResponseDto(
        UUID itemId,
        UUID queueId,
        String segment,
        String status,
        OffsetDateTime createdAt
) {
}

