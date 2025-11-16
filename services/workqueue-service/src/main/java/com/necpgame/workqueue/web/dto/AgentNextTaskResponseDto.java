package com.necpgame.workqueue.web.dto;

public record AgentNextTaskResponseDto(
        QueueItemSummaryDto summary,
        String recommendedStatus,
        int defaultTtlMinutes,
        boolean requiresAcceptance
) {
}

