package com.necpgame.workqueue.service.command;

import com.necpgame.workqueue.domain.AgentEntity;
import java.time.OffsetDateTime;
import java.util.UUID;

public record QueueItemUpdateCommand(
        UUID itemId,
        AgentEntity actor,
        String statusCode,
        Long expectedVersion,
        String note,
        String payload,
        AgentEntity assignedTo,
        String metadata,
        OffsetDateTime occurredAt,
        boolean clearAssignee
) {
    public QueueItemUpdateCommand {
        if (itemId == null) {
            throw new IllegalArgumentException("itemId required");
        }
        if (actor == null) {
            throw new IllegalArgumentException("actor required");
        }
        if (statusCode == null || statusCode.isBlank()) {
            throw new IllegalArgumentException("statusCode required");
        }
        if (expectedVersion == null) {
            throw new IllegalArgumentException("expectedVersion required");
        }
    }
}



