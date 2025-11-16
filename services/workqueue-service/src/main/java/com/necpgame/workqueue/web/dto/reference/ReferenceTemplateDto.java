package com.necpgame.workqueue.web.dto.reference;

import java.time.OffsetDateTime;

public record ReferenceTemplateDto(
        String code,
        String title,
        String body,
        String type,
        String sourcePath,
        String version,
        String contentHash,
        OffsetDateTime updatedAt
) {
}

