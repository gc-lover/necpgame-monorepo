package com.necpgame.workqueue.web.dto.release;

import com.necpgame.workqueue.web.dto.content.EnumValueDto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ReleaseRunStepDto(
        UUID id,
        int sortOrder,
        String ownerRole,
        String actionDescription,
        OffsetDateTime dueDate,
        EnumValueDto status,
        OffsetDateTime completedAt,
        Object metadata
) {
}


