package com.necpgame.workqueue.web.dto.agent;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AgentSummaryDto(
        UUID id,
        String roleKey,
        String displayName,
        String contact,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}


