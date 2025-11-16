package com.necpgame.workqueue.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record QueueItemUpdateRequestDto(
        @NotBlank String statusCode,
        @NotNull Long expectedVersion,
        String note,
        String payload,
        UUID assignedTo,
        String metadata
) {
}

