package com.necpgame.workqueue.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import com.necpgame.workqueue.domain.npc.NpcDataEntity;
import com.necpgame.workqueue.domain.npc.NpcDialogueLinkEntity;
import com.necpgame.workqueue.domain.npc.NpcInventoryItemEntity;
import com.necpgame.workqueue.domain.npc.NpcScheduleEntryEntity;
import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import com.necpgame.workqueue.repository.ContentEntryRepository;
import com.necpgame.workqueue.repository.npc.NpcDataRepository;
import com.necpgame.workqueue.repository.npc.NpcDialogueLinkRepository;
import com.necpgame.workqueue.repository.npc.NpcInventoryItemRepository;
import com.necpgame.workqueue.repository.npc.NpcScheduleEntryRepository;
import com.necpgame.workqueue.security.AgentPrincipal;
import com.necpgame.workqueue.service.exception.ApiErrorDetail;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.service.model.ContentTaskContext;
import com.necpgame.workqueue.web.dto.content.EnumValueDto;
import com.necpgame.workqueue.web.dto.npc.NpcCommandRequestDto;
import com.necpgame.workqueue.web.dto.npc.NpcDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class NpcCommandService {
    private static final String REQUIREMENT = "policy:content";
    private final ContentEntryRepository contentEntryRepository;
    private final NpcDataRepository npcDataRepository;
    private final NpcScheduleEntryRepository npcScheduleEntryRepository;
    private final NpcInventoryItemRepository npcInventoryItemRepository;
    private final NpcDialogueLinkRepository npcDialogueLinkRepository;
    private final EnumLookupService enumLookupService;
    private final AgentDirectoryService agentDirectoryService;
    private final ActivityLogService activityLogService;
    private final ContentQueryService contentQueryService;
    private final ObjectMapper objectMapper;
    private final ContentTaskCoordinator contentTaskCoordinator;

    @Transactional
    public NpcDetailDto create(AgentPrincipal principal, NpcCommandRequestDto request) {
        ContentEntryEntity content = requireNpcContent(request.contentCode());
        if (npcDataRepository.findById(content.getId()).isPresent()) {
            throw conflict("npc.exists", "Для данного контента уже есть NPC-данные");
        }
        AgentEntity actor = agentDirectoryService.requireAgent(principal.id());
        NpcDataEntity data = new NpcDataEntity();
        data.setEntity(content);
        applyTopLevel(data, request);
        npcDataRepository.save(data);
        replaceCollections(content, request);
        recordEvent(actor, content, "npc.created");
        return detail(content.getId());
    }

    @Transactional
    public NpcDetailDto update(AgentPrincipal principal, NpcCommandRequestDto request) {
        ContentEntryEntity content = requireNpcContent(request.contentCode());
        NpcDataEntity data = npcDataRepository.findById(content.getId())
                .orElseThrow(() -> conflict("npc.missing", "Сначала создайте запись NPC"));
        AgentEntity actor = agentDirectoryService.requireAgent(principal.id());
        applyTopLevel(data, request);
        npcDataRepository.save(data);
        replaceCollections(content, request);
        recordEvent(actor, content, "npc.updated");
        return detail(content.getId());
    }

    @Transactional(readOnly = true)
    public NpcDetailDto detail(UUID contentId) {
        var contentDetail = contentQueryService.getDetail(contentId);
        NpcDataEntity data = npcDataRepository.findById(contentId)
                .orElseThrow(() -> notFound("npc.not_found", "NPC не найден"));
        List<NpcScheduleEntryEntity> schedule = npcScheduleEntryRepository.findByNpc_IdOrderByDayTimeRangeAsc(contentId);
        List<NpcInventoryItemEntity> inventory = npcInventoryItemRepository.findByNpc_Id(contentId);
        List<NpcDialogueLinkEntity> dialogueLinks = npcDialogueLinkRepository.findByNpc_IdOrderByPriorityDesc(contentId);

        return new NpcDetailDto(
                contentDetail.summary(),
                toEnum(data.getAlignment()),
                toEnum(data.getBehavior()),
                data.getFaction() == null ? null : data.getFaction().getId(),
                data.getRoleTitle(),
                data.getLevel(),
                data.getPowerScore(),
                readMap(data.getVendorCatalogJson()),
                readMap(data.getScheduleMetadataJson()),
                readMap(data.getDialogueProfileJson()),
                readMap(data.getMetadataJson()),
                schedule.stream()
                        .map(entry -> new NpcDetailDto.ScheduleEntryDto(
                                entry.getId(),
                                entry.getDayTimeRange(),
                                entry.getLocation() == null ? null : entry.getLocation().getId(),
                                readMap(entry.getSchedulePayloadJson())
                        ))
                        .toList(),
                inventory.stream()
                        .map(item -> new NpcDetailDto.InventoryItemDto(
                                item.getId(),
                                item.getItem().getId(),
                                item.getQuantity(),
                                item.getRestockIntervalMinutes(),
                                item.getPriceOverride(),
                                readMap(item.getMetadataJson())
                        ))
                        .toList(),
                dialogueLinks.stream()
                        .map(link -> new NpcDetailDto.DialogueLinkDto(
                                link.getId(),
                                link.getDialogue().getId(),
                                link.getPriority(),
                                readMap(link.getConditionsJson()),
                                readMap(link.getMetadataJson())
                        ))
                        .toList()
        );
    }

    private void applyTopLevel(NpcDataEntity entity, NpcCommandRequestDto request) {
        entity.setAlignment(resolveEnum("npc_alignment", request.alignmentCode()));
        entity.setBehavior(resolveEnum("npc_behavior", request.behaviorCode()));
        entity.setFaction(resolveContentReference(request.factionEntityId()));
        entity.setRoleTitle(request.roleTitle());
        entity.setLevel(request.level());
        entity.setPowerScore(request.powerScore() == null ? null : request.powerScore().stripTrailingZeros());
        entity.setVendorCatalogJson(writeJson(request.vendorCatalog(), "vendorCatalog"));
        entity.setScheduleMetadataJson(writeJson(request.scheduleMetadata(), "scheduleMetadata"));
        entity.setDialogueProfileJson(writeJson(request.dialogueProfile(), "dialogueProfile"));
        entity.setMetadataJson(writeJson(request.metadata(), "metadata"));
    }

    private void replaceCollections(ContentEntryEntity npc, NpcCommandRequestDto request) {
        UUID npcId = npc.getId();
        npcScheduleEntryRepository.deleteByNpc_Id(npcId);
        if (request.schedule() != null) {
            request.schedule().forEach(dto -> {
                NpcScheduleEntryEntity entry = new NpcScheduleEntryEntity();
                entry.setNpc(npc);
                entry.setDayTimeRange(dto.dayTimeRange());
                entry.setLocation(resolveContentReference(dto.locationEntityId()));
                entry.setSchedulePayloadJson(writeJson(dto.payload(), "schedule.payload"));
                npcScheduleEntryRepository.save(entry);
            });
        }

        npcInventoryItemRepository.deleteByNpc_Id(npcId);
        if (request.inventory() != null) {
            request.inventory().forEach(dto -> {
                NpcInventoryItemEntity entity = new NpcInventoryItemEntity();
                entity.setNpc(npc);
                entity.setItem(requireContent(dto.itemEntityId()));
                entity.setQuantity(dto.quantity());
                entity.setRestockIntervalMinutes(dto.restockIntervalMinutes());
                entity.setPriceOverride(strip(dto.priceOverride()));
                entity.setMetadataJson(writeJson(dto.metadata(), "inventory.metadata"));
                npcInventoryItemRepository.save(entity);
            });
        }

        npcDialogueLinkRepository.deleteByNpc_Id(npcId);
        if (request.dialogueLinks() != null) {
            request.dialogueLinks().forEach(dto -> {
                NpcDialogueLinkEntity entity = new NpcDialogueLinkEntity();
                entity.setNpc(npc);
                entity.setDialogue(requireContent(dto.dialogueEntityId()));
                entity.setPriority(dto.priority());
                entity.setConditionsJson(writeJson(dto.conditions(), "dialogue.conditions"));
                entity.setMetadataJson(writeJson(dto.metadata(), "dialogue.metadata"));
                npcDialogueLinkRepository.save(entity);
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
                        "NPC готов к передаче",
                        List.of(
                                "/api/content/entities/" + content.getId(),
                                "/api/npcs/" + content.getId()
                        ),
                        Map.of(
                                "domain", "npcs",
                                "operation", eventType.endsWith(".created") ? "create" : "update"
                        ),
                        eventType.endsWith(".created") ? 4 : 3
                )
        );
    }

    private ContentEntryEntity requireNpcContent(String code) {
        ContentEntryEntity entity = contentEntryRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> notFound("content.not_found", "Контент с указанным кодом не найден"));
        if (entity.getEntityType() == null || !"npc".equalsIgnoreCase(entity.getEntityType().getCode())) {
            throw new ApiErrorException(
                    HttpStatus.BAD_REQUEST,
                    "npc.invalid_type",
                    "Контент должен иметь тип npc",
                    List.of(REQUIREMENT),
                    List.of(new ApiErrorDetail("contentCode", "Используйте сущность типа npc"))
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
            return null;
        }
        return contentEntryRepository.findById(id)
                .orElseThrow(() -> notFound("content.not_found", "Связанный контент не найден"));
    }

    private ContentEntryEntity resolveContentReference(UUID id) {
        return id == null ? null : requireContent(id);
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
                    "npc.serialization_error",
                    "Некорректный JSON объект",
                    List.of(REQUIREMENT),
                    List.of(new ApiErrorDetail(field, e.getMessage()))
            );
        }
    }

    private BigDecimal strip(BigDecimal value) {
        return value == null ? null : value.stripTrailingZeros();
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

