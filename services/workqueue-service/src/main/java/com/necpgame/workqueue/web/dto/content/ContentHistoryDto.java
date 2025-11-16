package com.necpgame.workqueue.web.dto.content;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ContentHistoryDto(
        UUID id,
        String version,
        OffsetDateTime changedAt,
        String changedBy,
        String summary,
        String diff
) {
}


