package com.necpgame.workqueue.web.dto.content.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;

public record ContentCommandRequestDto(
        @NotBlank @Size(max = 128) String code,
        @NotBlank @Size(max = 256) String title,
        String summary,
        @NotBlank String typeCode,
        @NotBlank String statusCode,
        String categoryCode,
        @NotBlank String visibilityCode,
        String riskLevelCode,
        @Size(max = 64) String ownerRole,
        @NotBlank @Size(max = 32) String version,
        @NotNull OffsetDateTime lastUpdated,
        @Size(max = 512) String sourceDocument,
        List<@NotBlank String> tags,
        List<@NotBlank String> topics,
        Map<String, Object> metadata
) {
}


