package com.necpgame.workqueue.web.dto.knowledge;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record KnowledgeDocumentDto(
        UUID id,
        String code,
        String sourcePath,
        String category,
        String documentType,
        String format,
        String title,
        String checksum,
        String body,
        List<String> tags,
        OffsetDateTime updatedAt
) {
}

