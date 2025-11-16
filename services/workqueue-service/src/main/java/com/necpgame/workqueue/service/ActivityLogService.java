package com.necpgame.workqueue.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.domain.ActivityLogEntity;
import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ActivityLogService {
    private final ActivityLogRepository activityLogRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void recordItemEvent(AgentEntity actor, QueueItemEntity item, String eventType, String metadata) {
        java.util.Objects.requireNonNull(item, "item required");
        java.util.Objects.requireNonNull(eventType, "eventType required");
        Map<String, Object> payload = new HashMap<>();
        payload.put("queueId", item.getQueue().getId());
        payload.put("status", eventType);
        if (metadata != null) {
            payload.put("metadata", metadata);
        }
        save(actor, "queue_item", item.getId(), eventType, payload);
    }

    @Transactional
    public void recordContentEvent(AgentEntity actor, ContentEntryEntity content, String eventType, Map<String, Object> metadata) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("contentCode", content.getCode());
        payload.put("version", content.getVersion());
        if (metadata != null) {
            payload.putAll(metadata);
        }
        save(actor, "content_entity", content.getId(), eventType, payload);
    }

    private void save(AgentEntity actor, String entityType, UUID entityId, String eventType, Map<String, Object> payload) {
        String serialized = serialize(payload);
        ActivityLogEntity entity = ActivityLogEntity.builder()
                .id(UUID.randomUUID())
                .actor(actor)
                .entityType(entityType)
                .entityId(entityId)
                .eventType(eventType)
                .payload(serialized)
                .recordedAt(OffsetDateTime.now())
                .build();
        activityLogRepository.save(entity);
    }

    private String serialize(Map<String, Object> payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }
}

