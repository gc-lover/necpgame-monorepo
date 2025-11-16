package com.necpgame.workqueue.web.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AgentTaskReleaseRequestDto(
        @NotNull UUID itemId,
        @NotNull Long expectedVersion,
        String note,
        String statusCode
) {
}

