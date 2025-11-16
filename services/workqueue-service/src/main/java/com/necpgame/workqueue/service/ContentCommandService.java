package com.necpgame.workqueue.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import com.necpgame.workqueue.repository.ContentEntryRepository;
import com.necpgame.workqueue.security.AgentPrincipal;
import com.necpgame.workqueue.service.exception.ApiErrorDetail;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.service.model.ContentTaskContext;
import com.necpgame.workqueue.web.dto.content.command.ContentCommandRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContentCommandService {
    private static final String REQUIREMENT_CONTENT_POLICY = "policy:content";
    private static final List<String> REQUIREMENTS = List.of(REQUIREMENT_CONTENT_POLICY);
    private final ContentEntryRepository contentEntryRepository;
    private final EnumLookupService enumLookupService;
    private final ObjectMapper objectMapper;
    private final AgentDirectoryService agentDirectoryService;
    private final ActivityLogService activityLogService;
    private final ContentTaskCoordinator contentTaskCoordinator;

    @Transactional
    public ContentEntryEntity create(AgentPrincipal principal, ContentCommandRequestDto request) {
        AgentEntity actor = agentDirectoryService.requireAgent(principal.id());
        ContentEntryEntity entity = new ContentEntryEntity();
        entity.setId(UUID.randomUUID());
        entity.setCreatedAt(OffsetDateTime.now());
        apply(entity, request, true);
        ContentEntryEntity saved = contentEntryRepository.save(entity);
        recordAndEnqueue(actor, saved, "content.created", "Автоматический старт Vision этапа", "create", 4);
        return saved;
    }

    @Transactional
    public ContentEntryEntity update(AgentPrincipal principal, UUID id, ContentCommandRequestDto request) {
        AgentEntity actor = agentDirectoryService.requireAgent(principal.id());
        ContentEntryEntity entity = contentEntryRepository.findById(id)
                .orElseThrow(() -> new ApiErrorException(
                        HttpStatus.NOT_FOUND,
                        "content.not_found",
                        "Сущность контента не найдена",
                        REQUIREMENTS,
                        List.of(new ApiErrorDetail("id", "Не удалось найти запись"))
                ));
        apply(entity, request, false);
        ContentEntryEntity saved = contentEntryRepository.save(entity);
        recordAndEnqueue(actor, saved, "content.updated", "Обновление знаний готово к следующему сегменту", "update", 3);
        return saved;
    }

    private void ensureCodeUnique(String code) {
        if (code == null || code.isBlank()) {
            throw new ApiErrorException(
                    HttpStatus.BAD_REQUEST,
                    "content.invalid_code",
                    "Код обязателен",
                    REQUIREMENTS,
                    List.of(new ApiErrorDetail("code", "Укажите код сущности"))
            );
        }
        contentEntryRepository.findByCodeIgnoreCase(code.trim())
                .ifPresent(existing -> {
                    throw new ApiErrorException(
                            HttpStatus.CONFLICT,
                            "content.code_exists",
                            "Сущность с таким кодом уже существует",
                            REQUIREMENTS,
                            List.of(new ApiErrorDetail("code", "Используйте уникальный код"))
                    );
                });
    }

    private void apply(ContentEntryEntity entity, ContentCommandRequestDto request, boolean creating) {
        if (creating) {
            ensureCodeUnique(request.code());
        } else {
            ensureCodeUniqueForUpdate(entity.getId(), request.code());
        }
        entity.setCode(normalizeCode(request.code()));
        entity.setTitle(request.title());
        entity.setSummary(request.summary());
        entity.setEntityType(enumLookupService.require("entity_type", request.typeCode()));
        entity.setStatus(enumLookupService.require("entity_status", request.statusCode()));
        entity.setVisibility(enumLookupService.require("entity_visibility", request.visibilityCode()));
        entity.setRiskLevel(resolveOptionalEnum("risk_level", request.riskLevelCode()));
        entity.setOwnerRole(request.ownerRole());
        entity.setVersion(request.version());
        entity.setLastUpdated(request.lastUpdated());
        entity.setSourceDocument(request.sourceDocument());
        entity.setTags(writeJsonArray(request.tags()));
        entity.setTopics(writeJsonArray(request.topics()));
        entity.setMetadataJson(writeJsonObject(request.metadata()));
        entity.setUpdatedAt(OffsetDateTime.now());
        entity.setCategory(resolveCategory(request));
        if (creating && entity.getCreatedAt() == null) {
            entity.setCreatedAt(OffsetDateTime.now());
        }
    }

    private void ensureCodeUniqueForUpdate(UUID entityId, String code) {
        if (code == null || code.isBlank()) {
            return;
        }
        contentEntryRepository.findByCodeIgnoreCase(code.trim())
                .filter(existing -> !existing.getId().equals(entityId))
                .ifPresent(existing -> {
                    throw new ApiErrorException(
                            HttpStatus.CONFLICT,
                            "content.code_exists",
                            "Сущность с таким кодом уже существует",
                            REQUIREMENTS,
                            List.of(new ApiErrorDetail("code", "Используйте уникальный код"))
                    );
                });
    }

    private EnumValueEntity resolveCategory(ContentCommandRequestDto request) {
        if (request.categoryCode() == null || request.categoryCode().isBlank()) {
            return null;
        }
        String type = request.typeCode() == null ? "" : request.typeCode().toLowerCase(Locale.ROOT);
        return switch (type) {
            case "quest" -> enumLookupService.require("quest_category", request.categoryCode());
            case "item" -> enumLookupService.require("item_category", request.categoryCode());
            default -> throw new ApiErrorException(
                    HttpStatus.BAD_REQUEST,
                    "content.unsupported_category",
                    "Категория допустима только для типов quest или item",
                    REQUIREMENTS,
                    List.of(new ApiErrorDetail("categoryCode", "Удалите поле или смените тип"))
            );
        };
    }

    private EnumValueEntity resolveOptionalEnum(String group, String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        return enumLookupService.require(group, code);
    }

    private String writeJsonArray(List<String> items) {
        List<String> safe = items == null ? List.of() : items.stream()
                .filter(value -> value != null && !value.isBlank())
                .map(String::trim)
                .toList();
        try {
            return objectMapper.writeValueAsString(safe);
        } catch (JsonProcessingException e) {
            throw new ApiErrorException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "content.serialization_error",
                    "Не удалось сериализовать коллекцию",
                    REQUIREMENTS,
                    List.of(new ApiErrorDetail("tags", e.getMessage()))
            );
        }
    }

    private String writeJsonObject(Map<String, Object> metadata) {
        Map<String, Object> safe = metadata == null ? Map.of() : metadata;
        try {
            return objectMapper.writeValueAsString(safe);
        } catch (JsonProcessingException e) {
            throw new ApiErrorException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "content.serialization_error",
                    "Не удалось сериализовать объект",
                    REQUIREMENTS,
                    List.of(new ApiErrorDetail("metadata", e.getMessage()))
            );
        }
    }

    private String normalizeCode(String code) {
        return code == null ? null : code.trim().toLowerCase(Locale.ROOT);
    }

    private void recordAndEnqueue(AgentEntity actor,
                                  ContentEntryEntity content,
                                  String eventType,
                                  String note,
                                  String operation,
                                  int priority) {
        activityLogService.recordContentEvent(
                actor,
                content,
                eventType,
                Map.of("path", "/api/content/entities/" + content.getId(), "operation", operation)
        );
        contentTaskCoordinator.enqueueContentChange(
                actor,
                content,
                new ContentTaskContext(
                        eventType,
                        note,
                        List.of("/api/content/entities/" + content.getId()),
                        Map.of("domain", "content", "operation", operation),
                        priority
                )
        );
    }
}


