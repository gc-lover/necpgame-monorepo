package com.necpgame.workqueue.service.model;

import java.util.List;

public record AgentPreference(
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

