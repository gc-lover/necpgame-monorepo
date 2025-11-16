package com.necpgame.workqueue.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.workqueue.domain.AgentEntity;
import com.necpgame.workqueue.domain.content.ContentEntryEntity;
import com.necpgame.workqueue.domain.quest.QuestBranchEntity;
import com.necpgame.workqueue.domain.quest.QuestDataEntity;
import com.necpgame.workqueue.domain.quest.QuestRewardEntity;
import com.necpgame.workqueue.domain.quest.QuestStageEntity;
import com.necpgame.workqueue.domain.quest.QuestWorldEffectEntity;
import com.necpgame.workqueue.domain.reference.EnumValueEntity;
import com.necpgame.workqueue.repository.ContentEntryRepository;
import com.necpgame.workqueue.repository.quest.QuestBranchRepository;
import com.necpgame.workqueue.repository.quest.QuestDataRepository;
import com.necpgame.workqueue.repository.quest.QuestRewardRepository;
import com.necpgame.workqueue.repository.quest.QuestStageRepository;
import com.necpgame.workqueue.repository.quest.QuestWorldEffectRepository;
import com.necpgame.workqueue.security.AgentPrincipal;
import com.necpgame.workqueue.service.exception.ApiErrorDetail;
import com.necpgame.workqueue.service.exception.ApiErrorException;
import com.necpgame.workqueue.service.model.ContentTaskContext;
import com.necpgame.workqueue.web.dto.quest.QuestCommandRequestDto;
import com.necpgame.workqueue.web.dto.quest.QuestDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuestCommandService {
    private static final String REQUIREMENT = "policy:content";
    private final ContentEntryRepository contentEntryRepository;
    private final QuestDataRepository questDataRepository;
    private final QuestStageRepository questStageRepository;
    private final QuestRewardRepository questRewardRepository;
    private final QuestBranchRepository questBranchRepository;
    private final QuestWorldEffectRepository questWorldEffectRepository;
    private final EnumLookupService enumLookupService;
    private final AgentDirectoryService agentDirectoryService;
    private final ActivityLogService activityLogService;
    private final ContentQueryService contentQueryService;
    private final ObjectMapper objectMapper;
    private final ContentTaskCoordinator contentTaskCoordinator;

    @Transactional
    public QuestDetailDto create(AgentPrincipal principal, QuestCommandRequestDto request) {
        ContentEntryEntity content = requireQuestContent(request.contentCode());
        if (questDataRepository.findById(content.getId()).isPresent()) {
            throw conflict("quest_data.exists", "Для данного контента уже есть типизированные данные");
        }
        AgentEntity actor = agentDirectoryService.requireAgent(principal.id());
        QuestDataEntity data = new QuestDataEntity();
        data.setEntity(content);
        applyTopLevel(data, request);
        questDataRepository.save(data);
        replaceChildren(content, request);
        recordEvent(actor, content, "quest.created");
        return detail(content.getId());
    }

    @Transactional
    public QuestDetailDto update(AgentPrincipal principal, QuestCommandRequestDto request) {
        ContentEntryEntity content = requireQuestContent(request.contentCode());
        QuestDataEntity data = questDataRepository.findById(content.getId())
                .orElseThrow(() -> conflict("quest_data.missing", "Сначала создайте запись для квеста"));
        AgentEntity actor = agentDirectoryService.requireAgent(principal.id());
        applyTopLevel(data, request);
        questDataRepository.save(data);
        replaceChildren(content, request);
        recordEvent(actor, content, "quest.updated");
        return detail(content.getId());
    }

    @Transactional(readOnly = true)
    public QuestDetailDto detail(UUID questId) {
        var contentDetail = contentQueryService.getDetail(questId);
        QuestDataEntity data = questDataRepository.findById(questId)
                .orElseThrow(() -> notFound("quest.not_found", "Квест не найден"));

        List<QuestStageEntity> stages = questStageRepository.findByQuestEntity_IdOrderByStageIndexAsc(questId);
        List<QuestRewardEntity> rewards = questRewardRepository.findByQuestEntity_Id(questId);
        List<QuestBranchEntity> branches = questBranchRepository.findByQuestEntity_Id(questId);
        List<QuestWorldEffectEntity> effects = questWorldEffectRepository.findByQuestEntity_Id(questId);

        return new QuestDetailDto(
                contentDetail.summary(),
                data.getSegment(),
                toEnum(data.getCategory()),
                toEnum(data.getDifficulty()),
                data.getLevelMin(),
                data.getLevelMax(),
                data.getEstimatedDurationMin(),
                data.getEstimatedDurationMax(),
                data.isRepeatable(),
                data.getRecommendedPlayers(),
                data.getStartNpcId(),
                data.getEndNpcId(),
                data.getStartLocationId(),
                data.getEndLocationId(),
                readMap(data.getPrerequisitesJson()),
                readMap(data.getMetadataJson()),
                stages.stream().map(this::toStageDetail).toList(),
                rewards.stream().map(this::toRewardDetail).toList(),
                branches.stream().map(this::toBranchDetail).toList(),
                effects.stream().map(this::toEffectDetail).toList()
        );
    }

    private void applyTopLevel(QuestDataEntity entity, QuestCommandRequestDto request) {
        entity.setSegment(request.segment());
        entity.setCategory(resolveEnum("quest_category", request.categoryCode()));
        entity.setDifficulty(resolveEnum("quest_difficulty", request.difficultyCode()));
        entity.setLevelMin(request.levelMin());
        entity.setLevelMax(request.levelMax());
        entity.setEstimatedDurationMin(request.estimatedDurationMin());
        entity.setEstimatedDurationMax(request.estimatedDurationMax());
        entity.setRepeatable(Boolean.TRUE.equals(request.repeatable()));
        entity.setRecommendedPlayers(request.recommendedPlayers());
        entity.setStartNpcId(request.startNpcId());
        entity.setEndNpcId(request.endNpcId());
        entity.setStartLocationId(request.startLocationId());
        entity.setEndLocationId(request.endLocationId());
        entity.setPrerequisitesJson(writeJson(request.prerequisites()));
        entity.setMetadataJson(writeJson(request.metadata()));
    }

    private void replaceChildren(ContentEntryEntity content, QuestCommandRequestDto request) {
        UUID questId = content.getId();
        questStageRepository.deleteByQuestId(questId);
        questRewardRepository.deleteByQuestId(questId);
        questBranchRepository.deleteByQuestId(questId);
        questWorldEffectRepository.deleteByQuestId(questId);

        if (request.stages() != null) {
            request.stages().forEach(dto -> questStageRepository.save(QuestStageEntity.builder()
                    .id(UUID.randomUUID())
                    .questEntity(content)
                    .stageIndex(dto.index())
                    .title(dto.title())
                    .description(dto.description())
                    .objectiveType(resolveEnum("quest_objective_type", dto.objectiveTypeCode()))
                    .targetEntityId(dto.targetEntityId())
                    .targetLocationEntityId(dto.targetLocationEntityId())
                    .optional(Boolean.TRUE.equals(dto.optional()))
                    .successConditionsJson(writeJson(dto.successConditions()))
                    .failureConditionsJson(writeJson(dto.failureConditions()))
                    .metadataJson(writeJson(dto.metadata()))
                    .build()));
        }

        if (request.rewards() != null) {
            request.rewards().forEach(dto -> questRewardRepository.save(QuestRewardEntity.builder()
                    .id(UUID.randomUUID())
                    .questEntity(content)
                    .rewardType(resolveEnum("quest_reward_type", dto.typeCode()))
                    .rewardEntityId(dto.rewardEntityId())
                    .amount(dto.amount() == null ? null : BigDecimal.valueOf(dto.amount()))
                    .metadataJson(writeJson(dto.metadata()))
                    .build()));
        }

        if (request.branches() != null) {
            request.branches().forEach(dto -> questBranchRepository.save(QuestBranchEntity.builder()
                    .id(UUID.randomUUID())
                    .questEntity(content)
                    .branchKey(dto.branchKey().toLowerCase(Locale.ROOT))
                    .fromStageIndex(dto.fromStageIndex())
                    .leadsToStageIndex(dto.leadsToStageIndex())
                    .triggerConditionsJson(writeJson(dto.triggerConditions()))
                    .notes(dto.notes())
                    .build()));
        }

        if (request.worldEffects() != null) {
            request.worldEffects().forEach(dto -> questWorldEffectRepository.save(QuestWorldEffectEntity.builder()
                    .id(UUID.randomUUID())
                    .questEntity(content)
                    .effectType(resolveEnum("quest_effect_type", dto.effectTypeCode()))
                    .targetEntityId(dto.targetEntityId())
                    .payloadJson(writeJson(dto.payload()))
                    .notes(dto.notes())
                    .build()));
        }
    }

    private QuestDetailDto.QuestStageDetailDto toStageDetail(QuestStageEntity entity) {
        return new QuestDetailDto.QuestStageDetailDto(
                entity.getId(),
                entity.getStageIndex(),
                entity.getTitle(),
                entity.getDescription(),
                toEnum(entity.getObjectiveType()),
                entity.getTargetEntityId(),
                entity.getTargetLocationEntityId(),
                entity.isOptional(),
                readMap(entity.getSuccessConditionsJson()),
                readMap(entity.getFailureConditionsJson()),
                readMap(entity.getMetadataJson())
        );
    }

    private QuestDetailDto.QuestRewardDetailDto toRewardDetail(QuestRewardEntity entity) {
        return new QuestDetailDto.QuestRewardDetailDto(
                entity.getId(),
                toEnum(entity.getRewardType()),
                entity.getRewardEntityId(),
                entity.getAmount(),
                readMap(entity.getMetadataJson())
        );
    }

    private QuestDetailDto.QuestBranchDetailDto toBranchDetail(QuestBranchEntity entity) {
        return new QuestDetailDto.QuestBranchDetailDto(
                entity.getId(),
                entity.getBranchKey(),
                entity.getFromStageIndex(),
                entity.getLeadsToStageIndex(),
                readMap(entity.getTriggerConditionsJson()),
                entity.getNotes()
        );
    }

    private QuestDetailDto.QuestWorldEffectDetailDto toEffectDetail(QuestWorldEffectEntity entity) {
        return new QuestDetailDto.QuestWorldEffectDetailDto(
                entity.getId(),
                toEnum(entity.getEffectType()),
                entity.getTargetEntityId(),
                readMap(entity.getPayloadJson()),
                entity.getNotes()
        );
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
                        "Квест готов к следующему сегменту",
                        List.of(
                                "/api/content/entities/" + content.getId(),
                                "/api/quests/" + content.getId()
                        ),
                        Map.of(
                                "domain", "quests",
                                "operation", eventType.endsWith(".created") ? "create" : "update"
                        ),
                        eventType.endsWith(".created") ? 4 : 3
                )
        );
    }

    private ContentEntryEntity requireQuestContent(String code) {
        ContentEntryEntity entity = contentEntryRepository.findByCodeIgnoreCase(code)
                .orElseThrow(() -> notFound("content.not_found", "Контент с указанным кодом не найден"));
        if (entity.getEntityType() == null || !"quest".equalsIgnoreCase(entity.getEntityType().getCode())) {
            throw new ApiErrorException(
                    HttpStatus.BAD_REQUEST,
                    "quest.invalid_type",
                    "Контент должен иметь тип quest",
                    List.of(REQUIREMENT),
                    List.of(new ApiErrorDetail("contentCode", "Укажите контент типа quest"))
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

    private String writeJson(Map<String, Object> source) {
        Map<String, Object> value = source == null ? Map.of() : source;
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new ApiErrorException(
                    HttpStatus.BAD_REQUEST,
                    "quest.serialization_error",
                    "Некорректный JSON объект",
                    List.of(REQUIREMENT),
                    List.of(new ApiErrorDetail("payload", e.getMessage()))
            );
        }
    }

    private Map<String, Object> readMap(String json) {
        if (json == null || json.isBlank()) {
            return Map.of();
        }
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            return Map.of();
        }
    }

    private ApiErrorException notFound(String code, String message) {
        return new ApiErrorException(HttpStatus.NOT_FOUND, code, message, List.of(REQUIREMENT), List.of());
    }

    private ApiErrorException conflict(String code, String message) {
        return new ApiErrorException(HttpStatus.CONFLICT, code, message, List.of(REQUIREMENT), List.of());
    }

    private com.necpgame.workqueue.web.dto.content.EnumValueDto toEnum(EnumValueEntity entity) {
        if (entity == null) {
            return null;
        }
        return new com.necpgame.workqueue.web.dto.content.EnumValueDto(
                entity.getId(),
                entity.getCode(),
                entity.getDisplayName(),
                entity.getDescription()
        );
    }
}


