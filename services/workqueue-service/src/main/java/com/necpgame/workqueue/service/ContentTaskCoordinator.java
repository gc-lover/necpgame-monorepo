package com.necpgame.workqueue.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.HandoffRuleEntity;
import com.necpgame.workqueue.domain.QueueEntity;
import com.necpgame.workqueue.domain.QueueItemEntity;
import com.necpgame.workqueue.domain.QueueItemStateEntity;
import com.necpgame.workqueue.domain.QueueItemTemplateEntity;
import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import com.necpgame.workqueue.repository.QueueItemRepository;
import com.necpgame.workqueue.repository.QueueItemStateRepository;
import com.necpgame.workqueue.repository.QueueItemTemplateRepository;
import com.necpgame.workqueue.repository.QueueRepository;
import com.necpgame.workqueue.service.exception.EntityNotFoundException;
import com.necpgame.workqueue.service.model.AgentPreference;
import com.necpgame.workqueue.service.model.ContentTaskContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentTaskCoordinator {
    private static final String STATUS_QUEUED = "queued";
    private final AgentPreferenceService agentPreferenceService;
    private final HandoffRuleService handoffRuleService;
    private final QueueRepository queueRepository;
    private final QueueItemRepository queueItemRepository;
    private final QueueItemStateRepository queueItemStateRepository;
    private final QueueItemTemplateRepository queueItemTemplateRepository;
    private final EnumCatalogService enumCatalogService;
    private final ActivityLogService activityLogService;
    private final ObjectMapper objectMapper;

    @Transactional
    public void enqueueContentChange(AgentEntity actor, ContentEntryEntity content, ContentTaskContext context) {
        if (actor == null || content == null || context == null) {
            return;
        }
        String currentSegment = resolveSegment(actor);
        if (currentSegment == null || currentSegment.isBlank()) {
            log.debug("Skip queue bridge: unresolved segment for role {}", actor.getRoleKey());
            return;
        }
        List<HandoffRuleEntity> rules = handoffRuleService.findRules(currentSegment, "completed");
        if (rules.isEmpty()) {
            log.debug("Skip queue bridge: no handoff rules for segment {}", currentSegment);
            return;
        }
        for (HandoffRuleEntity rule : rules) {
            QueueItemEntity item = createItemIfAbsent(actor, content, context, currentSegment, rule);
            if (item != null) {
                activityLogService.recordContentEvent(
                        actor,
                        content,
                        "content.queue_enqueued",
                        Map.of(
                                "queueItemId", item.getId(),
                                "nextSegment", rule.getNextSegment(),
                                "eventType", context.eventType()
                        )
                );
            }
        }
    }

    private QueueItemEntity createItemIfAbsent(AgentEntity actor,
                                               ContentEntryEntity content,
                                               ContentTaskContext context,
                                               String currentSegment,
                                               HandoffRuleEntity rule) {
        String targetSegment = normalize(rule.getNextSegment());
        if (targetSegment == null) {
            return null;
        }
        String externalRef = buildExternalRef(content, targetSegment);
        if (queueItemRepository.findByExternalRef(externalRef).isPresent()) {
            log.debug("Skip queue bridge: active item already exists for {}", externalRef);
            return null;
        }
        OffsetDateTime now = OffsetDateTime.now();
        QueueEntity queue = resolveQueue(targetSegment, actor, now);
        UUID statusValueId = enumCatalogService.requireTaskStatus(STATUS_QUEUED);
        QueueItemEntity item = QueueItemEntity.builder()
                .id(UUID.randomUUID())
                .queue(queue)
                .externalRef(externalRef)
                .title(buildTitle(content, targetSegment))
                .priority(context.priority())
                .payload(buildPayload(actor, content, currentSegment, targetSegment, context))
                .createdBy(actor)
                .dueAt(null)
                .lockedUntil(null)
                .createdAt(now)
                .updatedAt(now)
                .statusValueId(statusValueId)
                .version(0L)
                .build();
        queueItemRepository.save(item);
        QueueItemStateEntity state = QueueItemStateEntity.create(
                item,
                actor,
                STATUS_QUEUED,
                statusValueId,
                context.note() != null ? context.note() : context.eventType(),
                null,
                now
        );
        queueItemStateRepository.save(state);
        item.applyState(state);
        QueueItemEntity persisted = queueItemRepository.save(item);
        List<QueueItemTemplateEntity> templates = buildTemplates(persisted, rule, now);
        if (!templates.isEmpty()) {
            queueItemTemplateRepository.saveAll(templates);
        }
        return persisted;
    }

    private QueueEntity resolveQueue(String segment, AgentEntity actor, OffsetDateTime now) {
        return queueRepository.findBySegmentAndStatusCode(segment, STATUS_QUEUED)
                .orElseGet(() -> queueRepository.save(QueueEntity.builder()
                        .id(UUID.randomUUID())
                        .segment(segment)
                        .statusCode(STATUS_QUEUED)
                        .title(segment.toUpperCase(Locale.ROOT) + " :: queued")
                        .description("Создано автоматически после обновления контента")
                        .owner(actor)
                        .createdAt(now)
                        .updatedAt(now)
                        .build()));
    }

    private List<QueueItemTemplateEntity> buildTemplates(QueueItemEntity item, HandoffRuleEntity rule, OffsetDateTime now) {
        if (rule.getTemplateCodes() == null || rule.getTemplateCodes().isBlank()) {
            return List.of();
        }
        List<QueueItemTemplateEntity> templates = new ArrayList<>();
        for (String raw : rule.getTemplateCodes().split(",")) {
            String code = raw == null ? null : raw.trim();
            if (code == null || code.isEmpty()) {
                continue;
            }
            templates.add(QueueItemTemplateEntity.builder()
                    .id(UUID.randomUUID())
                    .item(item)
                    .templateCode(code)
                    .templateType(QueueItemTemplateEntity.TemplateType.REFERENCE)
                    .createdAt(now)
                    .build());
        }
        return templates;
    }

    private String buildPayload(AgentEntity actor,
                                ContentEntryEntity content,
                                String currentSegment,
                                String targetSegment,
                                ContentTaskContext context) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("contentId", content.getId());
        payload.put("contentCode", content.getCode());
        payload.put("contentTitle", content.getTitle());
        payload.put("contentType", content.getEntityType() != null ? content.getEntityType().getCode() : null);
        payload.put("contentVersion", content.getVersion());
        payload.put("sourceSegment", currentSegment);
        payload.put("targetSegment", targetSegment);
        payload.put("eventType", context.eventType());
        payload.put("knowledgeRefs", resolveRefs(content, context));
        payload.put("restRefs", resolveRefs(content, context));
        payload.put("initiator", Map.of(
                "agentId", actor.getId(),
                "roleKey", actor.getRoleKey(),
                "displayName", actor.getDisplayName()
        ));
        if (context.note() != null) {
            payload.put("note", context.note());
        }
        if (!context.attributes().isEmpty()) {
            payload.put("attributes", context.attributes());
        }
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize payload for content {}", content.getCode(), e);
            return "{}";
        }
    }

    private List<String> resolveRefs(ContentEntryEntity content, ContentTaskContext context) {
        if (!context.restRefs().isEmpty()) {
            return context.restRefs();
        }
        if (content.getId() == null) {
            return List.of();
        }
        return List.of("/api/content/entities/" + content.getId());
    }

    private String buildTitle(ContentEntryEntity content, String segment) {
        String base = content.getTitle() != null ? content.getTitle() : content.getCode();
        return base + " → " + segment.toUpperCase(Locale.ROOT);
    }

    private String buildExternalRef(ContentEntryEntity content, String targetSegment) {
        return "content/" + nullSafe(content.getCode()) + "::" + nullSafe(content.getVersion()) + "::" + targetSegment;
    }

    private String nullSafe(String value) {
        return value == null ? "na" : value.trim().toLowerCase(Locale.ROOT);
    }

    private String resolveSegment(AgentEntity actor) {
        return agentPreferenceService.find(actor.getId())
                .map(pref -> {
                    Optional<String> primary = pref.primarySegments().stream().findFirst();
                    if (primary.isPresent()) {
                        return normalize(primary.get());
                    }
                    return pref.fallbackSegments().stream().findFirst().map(this::normalize).orElseGet(() -> normalize(actor.getRoleKey()));
                })
                .orElseGet(() -> {
                    log.debug("Agent preference not configured for {}", actor.getId());
                    return normalize(actor.getRoleKey());
                });
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim().toLowerCase(Locale.ROOT);
        int idx = trimmed.indexOf('-');
        return idx > 0 ? trimmed.substring(0, idx) : trimmed;
    }
}

