package com.necpgame.workqueue.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import com.necpgame.workqueue.domain.world.WorldLocationEntity;
import com.necpgame.workqueue.domain.world.WorldLocationLinkEntity;
import com.necpgame.workqueue.domain.world.WorldSpawnPointEntity;
import com.necpgame.workqueue.repository.ContentEntryRepository;
import com.necpgame.workqueue.repository.world.WorldLocationLinkRepository;
import com.necpgame.workqueue.repository.world.WorldLocationRepository;
import com.necpgame.workqueue.repository.world.WorldSpawnPointRepository;
import com.necpgame.workqueue.security.AgentPrincipal;
import com.necpgame.workqueue.service.exception.ApiErrorDetail;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.service.model.ContentTaskContext;
import com.necpgame.workqueue.web.dto.content.EnumValueDto;
import com.necpgame.workqueue.web.dto.world.WorldLocationCommandRequestDto;
import com.necpgame.workqueue.web.dto.world.WorldLocationDetailDto;
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
public class WorldLocationCommandService {
    private static final String REQUIREMENT = "policy:content";
    private final ContentEntryRepository contentEntryRepository;
    private final WorldLocationRepository worldLocationRepository;
    private final WorldLocationLinkRepository worldLocationLinkRepository;
    private final WorldSpawnPointRepository worldSpawnPointRepository;
    private final EnumLookupService enumLookupService;
    private final AgentDirectoryService agentDirectoryService;
    private final ActivityLogService activityLogService;
    private final ContentQueryService contentQueryService;
    private final ObjectMapper objectMapper;
    private final ContentTaskCoordinator contentTaskCoordinator;

    @Transactional
    public WorldLocationDetailDto create(AgentPrincipal principal, WorldLocationCommandRequestDto request) {
        ContentEntryEntity content = requireLocationContent(request.contentCode());
        if (worldLocationRepository.findById(content.getId()).isPresent()) {
            throw conflict("world.location.exists", "Данные локации уже созданы");
        }
        AgentEntity actor = agentDirectoryService.requireAgent(principal.id());
        WorldLocationEntity entity = new WorldLocationEntity();
        entity.setEntity(content);
        applyTopLevel(entity, request);
        worldLocationRepository.save(entity);
        replaceCollections(content, request);
        recordEvent(actor, content, "world.location.created");
        return detail(content.getId());
    }

    @Transactional
    public WorldLocationDetailDto update(AgentPrincipal principal, WorldLocationCommandRequestDto request) {
        ContentEntryEntity content = requireLocationContent(request.contentCode());
        WorldLocationEntity entity = worldLocationRepository.findById(content.getId())
                .orElseThrow(() -> conflict("world.location.missing", "Сначала создайте локацию"));
        AgentEntity actor = agentDirectoryService.requireAgent(principal.id());
        applyTopLevel(entity, request);
        worldLocationRepository.save(entity);
        replaceCollections(content, request);
        recordEvent(actor, content, "world.location.updated");
        return detail(content.getId());
    }

    @Transactional(readOnly = true)
    public WorldLocationDetailDto detail(UUID contentId) {
        var contentDetail = contentQueryService.getDetail(contentId);
        WorldLocationEntity entity = worldLocationRepository.findById(contentId)
                .orElseThrow(() -> notFound("world.location.not_found", "Локация не найдена"));
        List<WorldLocationLinkEntity> links = worldLocationLinkRepository.findByFromLocation_Id(contentId);
        List<WorldSpawnPointEntity> spawns = worldSpawnPointRepository.findByLocation_Id(contentId);

        return new WorldLocationDetailDto(
                contentDetail.summary(),
                toEnum(entity.getRegion()),
                toEnum(entity.getBiome()),
                entity.getParentLocation() == null ? null : entity.getParentLocation().getId(),
                entity.getDangerLevel(),
                entity.getRecommendedLevelMin(),
                entity.getRecommendedLevelMax(),
                entity.getPopulationEstimate(),
                readMap(entity.getCoordinatesJson()),
                readMap(entity.getMetadataJson()),
                links.stream().map(link -> new WorldLocationDetailDto.LinkDto(
                        link.getId(),
                        link.getToLocation().getId(),
                        link.getLinkType(),
                        link.getTravelTimeMinutes(),
                        readMap(link.getMetadataJson())
                )).toList(),
                spawns.stream().map(spawn -> new WorldLocationDetailDto.SpawnPointDto(
                        spawn.getId(),
                        spawn.getSpawnType(),
                        spawn.getTarget() == null ? null : spawn.getTarget().getId(),
                        spawn.getRespawnSeconds(),
                        readMap(spawn.getConditionsJson()),
                        readMap(spawn.getMetadataJson())
                )).toList()
        );
    }

    private void applyTopLevel(WorldLocationEntity entity, WorldLocationCommandRequestDto request) {
        entity.setRegion(resolveEnum("world_region", request.regionCode()));
        entity.setBiome(resolveEnum("world_biome", request.biomeCode()));
        entity.setParentLocation(resolveContentReference(request.parentLocationId()));
        entity.setDangerLevel(request.dangerLevel());
        entity.setRecommendedLevelMin(request.recommendedLevelMin());
        entity.setRecommendedLevelMax(request.recommendedLevelMax());
        entity.setPopulationEstimate(request.populationEstimate());
        entity.setCoordinatesJson(writeJson(request.coordinates(), "coordinates"));
        entity.setMetadataJson(writeJson(request.metadata(), "metadata"));
    }

    private void replaceCollections(ContentEntryEntity location, WorldLocationCommandRequestDto request) {
        UUID locationId = location.getId();
        worldLocationLinkRepository.deleteByFromLocation_Id(locationId);
        if (request.links() != null) {
            request.links().forEach(dto -> {
                WorldLocationLinkEntity entity = new WorldLocationLinkEntity();
                entity.setFromLocation(location);
                entity.setToLocation(requireContent(dto.toLocationId()));
                entity.setLinkType(dto.linkType());
                entity.setTravelTimeMinutes(dto.travelTimeMinutes());
                entity.setMetadataJson(writeJson(dto.metadata(), "links.metadata"));
                worldLocationLinkRepository.save(entity);
            });
        }

        worldSpawnPointRepository.deleteByLocation_Id(locationId);
        if (request.spawnPoints() != null) {
            request.spawnPoints().forEach(dto -> {
                WorldSpawnPointEntity spawn = new WorldSpawnPointEntity();
                spawn.setLocation(location);
                spawn.setTarget(resolveContentReference(dto.targetEntityId()));
                spawn.setSpawnType(dto.spawnType());
                spawn.setRespawnSeconds(dto.respawnSeconds());
                spawn.setConditionsJson(writeJson(dto.conditions(), "spawn.conditions"));
                spawn.setMetadataJson(writeJson(dto.metadata(), "spawn.metadata"));
                worldSpawnPointRepository.save(spawn);
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
                        "Локация готова к дальнейшей работе",
                        List.of(
                                "/api/content/entities/" + content.getId(),
                                "/api/world/locations/" + content.getId()
                        ),
                        Map.of(
                                "domain", "world",
                                "entity", "location",
                                "operation", eventType.endsWith(".created") ? "create" : "update"
                        ),
                        eventType.endsWith(".created") ? 4 : 3
                )
        );
    }

    private ContentEntryEntity requireLocationContent(String code) {
        ContentEntryEntity entity = contentEntryRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> notFound("content.not_found", "Контент с указанным кодом не найден"));
        if (entity.getEntityType() == null || !"location".equalsIgnoreCase(entity.getEntityType().getCode())) {
            throw new ApiErrorException(
                    HttpStatus.BAD_REQUEST,
                    "world.location.invalid_type",
                    "Контент должен иметь тип location",
                    List.of(REQUIREMENT),
                    List.of(new ApiErrorDetail("contentCode", "Используйте сущность типа location"))
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

    private ContentEntryEntity requireContent(UUID id) {
        if (id == null) {
            throw new ApiErrorException(
                    HttpStatus.BAD_REQUEST,
                    "world.location.missing_reference",
                    "Требуется указать код контента",
                    List.of(REQUIREMENT),
                    List.of(new ApiErrorDetail("contentId", "Укажите существующий контент"))
            );
        }
        return contentEntryRepository.findById(id)
                .orElseThrow(() -> notFound("content.not_found", "Связанный контент не найден"));
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
                    "world.location.serialization_error",
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

