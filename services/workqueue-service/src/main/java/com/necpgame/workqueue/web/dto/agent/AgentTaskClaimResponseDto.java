package com.necpgame.workqueue.web.dto.agent;

import com.necpgame.workqueue.web.dto.ClaimInstructionsDto;
import com.necpgame.workqueue.web.dto.QueueItemDetailDto;

public record AgentTaskClaimResponseDto(
        QueueItemDetailDto item,
        String recommendedStatus,
        int ttlMinutes,
        boolean existingAssignment,
        ClaimInstructionsDto instructions
) {
}

