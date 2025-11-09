package com.necpgame.backjava.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.WorldEventEffectEntity;
import com.necpgame.backjava.entity.WorldEventEntity;
import com.necpgame.backjava.model.EventEffect;
import com.necpgame.backjava.model.EventEffectModifier;
import com.necpgame.backjava.model.WorldEvent;
import com.necpgame.backjava.model.WorldEventDetailed;
import com.necpgame.backjava.model.WorldEventDetailedAllOfFactionInvolvement;
import com.necpgame.backjava.model.WorldEventDetailedAllOfQuestHooks;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class WorldEventMapper {

    private static final TypeReference<List<String>> STRING_LIST = new TypeReference<>() {
    };
    private static final TypeReference<List<QuestHookPayload>> QUEST_HOOKS = new TypeReference<>() {
    };
    private static final TypeReference<List<FactionInvolvementPayload>> FACTION_INVOLVEMENT = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper;

    public WorldEventMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public WorldEvent toWorldEvent(WorldEventEntity entity) {
        WorldEvent model = new WorldEvent();
        model.setEventId(entity.getId().toString());
        model.setName(entity.getName());
        model.setType(WorldEvent.TypeEnum.fromValue(entity.getType().name()));
        model.setEra(entity.getEra());
        model.setSeverity(WorldEvent.SeverityEnum.fromValue(entity.getSeverity().name()));
        model.setStartDate(entity.getStartDate());
        if (entity.getEndDate() != null) {
            model.setEndDate(JsonNullable.of(entity.getEndDate()));
        } else {
            model.setEndDate(JsonNullable.undefined());
        }
        model.setIsActive(entity.isActive());
        model.setAffectedRegions(readStringList(entity.getAffectedRegionsJson()));
        return model;
    }

    public WorldEventDetailed toWorldEventDetailed(WorldEventEntity entity, List<WorldEventEffectEntity> effects) {
        WorldEventDetailed model = new WorldEventDetailed();
        model.setEventId(entity.getId().toString());
        model.setName(entity.getName());
        model.setType(WorldEventDetailed.TypeEnum.fromValue(entity.getType().name()));
        model.setEra(entity.getEra());
        model.setSeverity(WorldEventDetailed.SeverityEnum.fromValue(entity.getSeverity().name()));
        model.setStartDate(entity.getStartDate());
        if (entity.getEndDate() != null) {
            model.setEndDate(JsonNullable.of(entity.getEndDate()));
        } else {
            model.setEndDate(JsonNullable.undefined());
        }
        model.setIsActive(entity.isActive());
        model.setAffectedRegions(readStringList(entity.getAffectedRegionsJson()));
        model.setDescription(entity.getDescription());
        model.setLoreBackground(entity.getLoreBackground());
        model.setEffects(mapEffects(effects));
        model.setQuestHooks(mapQuestHooks(entity.getQuestHooksJson()));
        model.setFactionInvolvement(mapFactionInvolvement(entity.getFactionInvolvementJson()));
        return model;
    }

    public EventEffect toEventEffect(WorldEventEffectEntity entity) {
        EventEffect model = new EventEffect();
        model.setEffectId(entity.getId().toString());
        model.setEffectType(EventEffect.EffectTypeEnum.fromValue(entity.getEffectType().name()));
        model.setDescription(entity.getDescription());
        EventEffectModifier modifier = new EventEffectModifier();
        modifier.setType(entity.getModifierType());
        modifier.setValue(entity.getModifierValue());
        model.setModifier(modifier);
        model.setDuration(entity.getDuration());
        model.setStackable(entity.isStackable());
        return model;
    }

    private List<EventEffect> mapEffects(List<WorldEventEffectEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return Collections.emptyList();
        }
        return entities.stream()
                .map(this::toEventEffect)
                .collect(Collectors.toList());
    }

    private List<String> readStringList(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, STRING_LIST);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Не удалось разобрать список строк из JSON", ex);
        }
    }

    private List<WorldEventDetailedAllOfQuestHooks> mapQuestHooks(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            List<QuestHookPayload> payloads = objectMapper.readValue(json, QUEST_HOOKS);
            return payloads.stream()
                    .map(payload -> new WorldEventDetailedAllOfQuestHooks()
                            .questId(payload.questId)
                            .triggerCondition(payload.triggerCondition))
                    .collect(Collectors.toList());
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Не удалось разобрать квестовые хуки события", ex);
        }
    }

    private List<WorldEventDetailedAllOfFactionInvolvement> mapFactionInvolvement(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            List<FactionInvolvementPayload> payloads = objectMapper.readValue(json, FACTION_INVOLVEMENT);
            return payloads.stream()
                    .map(payload -> new WorldEventDetailedAllOfFactionInvolvement()
                            .faction(payload.faction)
                            .role(WorldEventDetailedAllOfFactionInvolvement.RoleEnum.fromValue(payload.role)))
                    .collect(Collectors.toList());
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Не удалось разобрать участие фракций в событии", ex);
        }
    }

    private record QuestHookPayload(String questId, String triggerCondition) {
    }

    private record FactionInvolvementPayload(String faction, String role) {
    }
}

