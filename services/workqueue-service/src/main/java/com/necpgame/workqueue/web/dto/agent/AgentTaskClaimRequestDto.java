package com.necpgame.workqueue.web.dto.agent;

import jakarta.validation.constraints.Min;

import java.util.List;

public record AgentTaskClaimRequestDto(
        List<String> segments,
        @Min(0) Integer priorityFloor
) {
}

