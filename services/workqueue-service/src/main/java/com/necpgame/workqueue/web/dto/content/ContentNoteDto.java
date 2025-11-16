package com.necpgame.workqueue.web.dto.content;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ContentNoteDto(
        UUID id,
        UUID authorId,
        String authorName,
        String noteText,
        OffsetDateTime createdAt
) {
}


