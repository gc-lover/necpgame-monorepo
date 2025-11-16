package com.necpgame.workqueue.web.dto.submission;

import java.util.UUID;

public record TaskSubmissionResponseDto(
        UUID itemId,
        String status,
        UUID nextItemId,
        String nextSegment
) {
}

