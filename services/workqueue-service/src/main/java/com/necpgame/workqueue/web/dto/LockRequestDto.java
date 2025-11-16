package com.necpgame.workqueue.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record LockRequestDto(
        @NotBlank String scope,
        UUID queueId,
        UUID itemId,
        @Min(5) long ttlSeconds
) {
}


