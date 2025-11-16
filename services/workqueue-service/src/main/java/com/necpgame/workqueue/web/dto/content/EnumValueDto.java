package com.necpgame.workqueue.web.dto.content;

import java.util.UUID;

public record EnumValueDto(
        UUID id,
        String code,
        String name,
        String description
) {
}


