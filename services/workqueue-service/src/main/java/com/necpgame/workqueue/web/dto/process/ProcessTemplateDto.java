package com.necpgame.workqueue.web.dto.process;

import com.necpgame.workqueue.web.dto.content.EnumValueDto;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.UUID;

public record ProcessTemplateDto(
        UUID id,
        EnumValueDto code,
        String name,
        String description,
        Map<String, Object> schema,
        String usageNotes,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}


