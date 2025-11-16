package com.necpgame.workqueue.web.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AgentTaskAcceptRequestDto(
        @NotNull UUID itemId,
        @NotNull Long expectedVersion,
        String statusCode,
        String note,
        String payload,
        String metadata
) {
}

