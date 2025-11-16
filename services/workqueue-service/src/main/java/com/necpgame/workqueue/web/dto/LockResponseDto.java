package com.necpgame.workqueue.web.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record LockResponseDto(
        UUID lockId,
        String token,
        OffsetDateTime expiresAt
) {
}


