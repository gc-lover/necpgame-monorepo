package com.necpgame.workqueue.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import com.necpgame.workqueue.domain.world.WorldEventEntity;
import com.necpgame.workqueue.domain.world.WorldEventRequirementEntity;
import com.necpgame.workqueue.repository.ContentEntryRepository;
import com.necpgame.workqueue.repository.world.WorldEventRepository;
import com.necpgame.workqueue.repository.world.WorldEventRequirementRepository;
import com.necpgame.workqueue.security.AgentPrincipal;
import com.necpgame.workqueue.service.exception.ApiErrorDetail;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.service.model.ContentTaskContext;
import com.necpgame.workqueue.web.dto.content.EnumValueDto;
import com.necpgame.workqueue.web.dto.world.WorldEventCommandRequestDto;
import com.necpgame.workqueue.web.dto.world.WorldEventDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class WorldEventCommandService {
    private static final String REQUIREMENT = "policy:content";
    private final ContentEntryRepository contentEntryRepository;
    private final WorldEventRepository worldEventRepository;
    private final WorldEventRequirementRepository worldEventRequirementRepository;
    private final EnumLookupService enumLookupService;
    private final AgentDirectoryService agentDirectoryService;
    private final ActivityLogService activityLogService;
    private final ContentQueryService contentQueryService;
    private final ObjectMapper objectMapper;
    private final ContentTaskCoordinator contentTaskCoordinator;

    @Transactional
    public WorldEventDetailDto create(AgentPrincipal principal, WorldEventCommandRequestDto request) {
        ContentEntryEntity content = requireEventContent(request.contentCode());
        if (worldEventRepository.findById(content.getId()).isPresent()) {
            throw conflict("world.event.exists", "Сущность события уже создана");
        }
        AgentEntity actor = agentDirectoryService.requireAgent(principal.id());
        WorldEventEntity entity = new WorldEventEntity();
        entity.setEntity(content);
        applyTopLevel(entity, request);
        worldEventRepository.save(entity);
        replaceRequirements(content, request);
        recordEvent(actor, content, "world.event.created");
        return detail(content.getId());
    }

    @Transactional
    public WorldEventDetailDto update(AgentPrincipal principal, WorldEventCommandRequestDto request) {
        ContentEntryEntity content = requireEventContent(request.contentCode());
        WorldEventEntity entity = worldEventRepository.findById(content.getId())
                .orElseThrow(() -> conflict("world.event.missing", "Сначала создайте событие"));
        AgentEntity actor = agentDirectoryService.requireAgent(principal.id());
        applyTopLevel(entity, request);
        worldEventRepository.save(entity);
        replaceRequirements(content, request);
        recordEvent(actor, content, "world.event.updated");
        return detail(content.getId());
    }

    @Transactional(readOnly = true)
    public WorldEventDetailDto detail(UUID contentId) {
        var contentDetail = contentQueryService.getDetail(contentId);
        WorldEventEntity entity = worldEventRepository.findById(contentId)
                .orElseThrow(() -> notFound("world.event.not_found", "Мировое событие не найдено"));
        List<WorldEventRequirementEntity> requirements = worldEventRequirementRepository.findByWorldEvent_Id(contentId);

        return new WorldEventDetailDto(
                contentDetail.summary(),
                toEnum(entity.getEventType()),
                toEnum(entity.getRegion()),
                entity.getLocation() == null ? null : entity.getLocation().getId(),
                entity.getDifficultyTier(),
                readMap(entity.getRecurrencePatternJson()),
                entity.getRewardEntity() == null ? null : entity.getRewardEntity().getId(),
                entity.getRewardDescription(),
                readMap(entity.getMetadataJson()),
                requirements.stream()
                        .map(req -> new WorldEventDetailDto.RequirementDto(
                                req.getId(),
                                readMap(req.getRequirementPayloadJson())
                        ))
                        .toList()
        );
    }

    private void applyTopLevel(WorldEventEntity entity, WorldEventCommandRequestDto request) {
        entity.setEventType(resolveEnum("world_event_type", request.eventTypeCode()));
        entity.setRegion(resolveEnum("world_region", request.regionCode()));
        entity.setLocation(resolveContentReference(request.locationEntityId()));
        entity.setDifficultyTier(request.difficultyTier());
        entity.setRecurrencePatternJson(writeJson(request.recurrencePattern(), "recurrencePattern"));
        entity.setRewardEntity(resolveContentReference(request.rewardEntityId()));
        entity.setRewardDescription(request.rewardDescription());
        entity.setMetadataJson(writeJson(request.metadata(), "metadata"));
    }

    private void replaceRequirements(ContentEntryEntity event, WorldEventCommandRequestDto request) {
        UUID eventId = event.getId();
        worldEventRequirementRepository.deleteByWorldEvent_Id(eventId);
        if (request.requirements() != null) {
            request.requirements().forEach(dto -> {
                WorldEventRequirementEntity requirement = new WorldEventRequirementEntity();
                requirement.setWorldEvent(event);
                requirement.setRequirementPayloadJson(writeJson(dto.payload(), "requirements.payload"));
                worldEventRequirementRepository.save(requirement);
            });
        }
    }

    private void recordEvent(AgentEntity actor, ContentEntryEntity content, String eventType) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("contentId", content.getId());
        payload.put("contentCode", content.getCode());
        activityLogService.recordContentEvent(actor, content, eventType, payload);
        contentTaskCoordinator.enqueueContentChange(
                actor,
                content,
                new ContentTaskContext(
                        eventType,
                        "Мировое событие готово к следующему этапу",
                        List.of(
                                "/api/content/entities/" + content.getId(),
                                "/api/world/events/" + content.getId()
                        ),
                        Map.of(
                                "domain", "world",
                                "entity", "event",
                                "operation", eventType.endsWith(".created") ? "create" : "update"
                        ),
                        eventType.endsWith(".created") ? 4 : 3
                )
        );
    }

    private ContentEntryEntity requireEventContent(String code) {
        ContentEntryEntity entity = contentEntryRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> notFound("content.not_found", "Контент с указанным кодом не найден"));
        if (entity.getEntityType() == null || !"world_event".equalsIgnoreCase(entity.getEntityType().getCode())) {
            throw new ApiErrorException(
                    HttpStatus.BAD_REQUEST,
                    "world.event.invalid_type",
                    "Контент должен иметь тип world_event",
                    List.of(REQUIREMENT),
                    List.of(new ApiErrorDetail("contentCode", "Используйте сущность типа world_event"))
            );
        }
        return entity;
    }

    private EnumValueEntity resolveEnum(String group, String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        return enumLookupService.require(group, code);
    }

    private ContentEntryEntity resolveContentReference(UUID id) {
        return id == null ? null : contentEntryRepository.findById(id)
                .orElseThrow(() -> notFound("content.not_found", "Связанный контент не найден"));
    }

    private Map<String, Object> readMap(String json) {
        if (json == null || json.isBlank()) {
            return Map.of();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            return Map.of();
        }
    }

    private String writeJson(Map<String, Object> source, String field) {
        Map<String, Object> safe = source == null ? Map.of() : source;
        try {
            return objectMapper.writeValueAsString(safe);
        } catch (JsonProcessingException e) {
            throw new ApiErrorException(
                    HttpStatus.BAD_REQUEST,
                    "world.event.serialization_error",
                    "Некорректный JSON объект",
                    List.of(REQUIREMENT),
                    List.of(new ApiErrorDetail(field, e.getMessage()))
            );
        }
    }

    private ApiErrorException notFound(String code, String message) {
        return new ApiErrorException(HttpStatus.NOT_FOUND, code, message, List.of(REQUIREMENT), List.of());
    }

    private ApiErrorException conflict(String code, String message) {
        return new ApiErrorException(HttpStatus.CONFLICT, code, message, List.of(REQUIREMENT), List.of());
    }

    private EnumValueDto toEnum(EnumValueEntity entity) {
        if (entity == null) {
            return null;
        }
        return new EnumValueDto(
                entity.getId(),
                entity.getCode(),
                entity.getDisplayName(),
                entity.getDescription()
        );
    }
}

