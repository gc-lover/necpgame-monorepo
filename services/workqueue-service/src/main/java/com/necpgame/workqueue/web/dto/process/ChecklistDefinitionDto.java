package com.necpgame.workqueue.web.dto.process;

import com.necpgame.workqueue.web.dto.content.EnumValueDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ChecklistDefinitionDto(
        UUID id,
        EnumValueDto code,
        String name,
        String description,
        boolean required,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        List<ChecklistItemDto> items
) {
}


