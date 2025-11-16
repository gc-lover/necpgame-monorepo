package com.necpgame.workqueue.web.dto.release;

import com.necpgame.workqueue.web.dto.content.EnumValueDto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ReleaseRunValidationDto(
        UUID id,
        String validationType,
        String description,
        EnumValueDto status,
        UUID validatedById,
        String validatedByName,
        OffsetDateTime validatedAt,
        Object results
) {
}


