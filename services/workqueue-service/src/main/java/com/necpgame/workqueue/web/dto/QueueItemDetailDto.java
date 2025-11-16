package com.necpgame.workqueue.web.dto;

import java.util.List;

public record QueueItemDetailDto(
        QueueItemSummaryDto summary,
        String payload,
        List<QueueItemStateDto> history
) {
}


