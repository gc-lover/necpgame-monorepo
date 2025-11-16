package com.necpgame.workqueue.web.dto.release;

import com.necpgame.workqueue.web.dto.content.EnumValueDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ReleaseRunDto(
        UUID id,
        String changeId,
        String title,
        UUID authorId,
        String authorName,
        OffsetDateTime releaseDate,
        EnumValueDto impactLevel,
        String summary,
        Object scopeDescription,
        String rollbackPlan,
        EnumValueDto status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        List<ReleaseRunStepDto> steps,
        List<ReleaseRunValidationDto> validations
) {
}


