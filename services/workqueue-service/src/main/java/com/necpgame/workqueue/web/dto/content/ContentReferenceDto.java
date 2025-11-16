package com.necpgame.workqueue.web.dto.content;

import java.util.UUID;

public record ContentReferenceDto(
        UUID id,
        String code,
        String title
) {
}


