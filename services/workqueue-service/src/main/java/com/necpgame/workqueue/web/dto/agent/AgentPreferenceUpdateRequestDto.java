package com.necpgame.workqueue.web.dto.agent;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AgentPreferenceUpdateRequestDto(
        @NotEmpty List<@NotBlank String> primarySegments,
        List<@NotBlank String> fallbackSegments,
        @NotEmpty List<@NotBlank String> pickupStatuses,
        @NotEmpty List<@NotBlank String> activeStatuses,
        @NotBlank String acceptStatus,
        @NotBlank String returnStatus,
        @NotNull @Min(1) @Max(720) Integer maxInProgressMinutes
) {
}


