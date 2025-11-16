package com.necpgame.workqueue.web.dto.content;

import java.util.UUID;

public record ContentSectionDto(
        UUID id,
        EnumValueDto sectionKey,
        String title,
        String body,
        Integer sortOrder,
        Object metadata
) {
}


