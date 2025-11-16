package com.necpgame.workqueue.web.dto.content;

import java.util.List;

public record ContentListResponseDto(
        List<ContentSummaryDto> items,
        long totalElements,
        int totalPages,
        int page,
        int size
) {
}


