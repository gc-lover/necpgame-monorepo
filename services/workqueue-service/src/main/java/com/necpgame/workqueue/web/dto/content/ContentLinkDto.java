package com.necpgame.workqueue.web.dto.content;

import java.util.UUID;

public record ContentLinkDto(
        UUID id,
        EnumValueDto relationType,
        UUID targetId,
        String targetCode,
        String targetTitle,
        String notes
) {
}


