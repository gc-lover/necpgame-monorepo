package com.necpgame.workqueue.web.dto;

import java.util.UUID;

public record AgentSummaryDto(
        UUID id,
        String roleKey,
        String displayName
) {
}


