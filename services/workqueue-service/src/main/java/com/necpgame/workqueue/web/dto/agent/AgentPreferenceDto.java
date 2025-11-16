package com.necpgame.workqueue.web.dto.agent;

import java.util.List;

public record AgentPreferenceDto(
        String roleKey,
        List<String> primarySegments,
        List<String> fallbackSegments,
        List<String> pickupStatuses,
        List<String> activeStatuses,
        String acceptStatus,
        String returnStatus,
        int maxInProgressMinutes
) {
}


