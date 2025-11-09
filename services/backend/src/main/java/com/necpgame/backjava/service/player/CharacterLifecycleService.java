package com.necpgame.backjava.service.player;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.necpgame.backjava.entity.CharacterAppearanceEntity;
import com.necpgame.backjava.entity.CharacterEntity;
import com.necpgame.backjava.entity.CharacterLocationEntity;
import com.necpgame.backjava.entity.CharacterSkillEntity;
import com.necpgame.backjava.entity.CharacterStatsEntity;
import com.necpgame.backjava.entity.CharacterStatsSnapshotEntity;
import com.necpgame.backjava.entity.CharacterStatusEntity;
import com.necpgame.backjava.exception.BusinessException;
import com.necpgame.backjava.exception.ErrorCode;
import com.necpgame.backjava.mapper.CharacterAppearanceMapperMS;
import com.necpgame.backjava.model.GameCharacterAppearance;
import com.necpgame.backjava.repository.CharacterLocationRepository;
import com.necpgame.backjava.repository.CharacterSkillRepository;
import com.necpgame.backjava.repository.CharacterStatsRepository;
import com.necpgame.backjava.repository.CharacterStatsSnapshotRepository;
import com.necpgame.backjava.repository.CharacterStatusRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CharacterLifecycleService {

    private final CharacterStatusRepository characterStatusRepository;
    private final CharacterStatsRepository characterStatsRepository;
    private final CharacterSkillRepository characterSkillRepository;
    private final CharacterLocationRepository characterLocationRepository;
    private final CharacterStatsSnapshotRepository characterStatsSnapshotRepository;
    private final CharacterAppearanceMapperMS characterAppearanceMapper;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public Map<UUID, CharacterStatusEntity> loadStatuses(Collection<CharacterEntity> characters) {
        if (characters.isEmpty()) {
            return Collections.emptyMap();
        }
        List<UUID> ids = characters.stream()
            .map(CharacterEntity::getId)
            .collect(Collectors.toList());
        return characterStatusRepository.findByCharacterIdIn(ids).stream()
            .collect(Collectors.toMap(CharacterStatusEntity::getCharacterId, it -> it));
    }

    public CharacterStatusEntity ensureStatus(UUID characterId) {
        return characterStatusRepository.findByCharacterId(characterId)
            .orElseGet(() -> {
                CharacterStatusEntity status = new CharacterStatusEntity();
                status.setCharacterId(characterId);
                return characterStatusRepository.save(status);
            });
    }

    public CharacterStatsEntity ensureStats(UUID characterId) {
        return characterStatsRepository.findByCharacterId(characterId)
            .orElseGet(() -> {
                CharacterStatsEntity stats = new CharacterStatsEntity();
                stats.setCharacterId(characterId);
                return characterStatsRepository.save(stats);
            });
    }

    public void ensureLocation(CharacterEntity character, CharacterLocationEntity template) {
        characterLocationRepository.findByCharacterId(character.getId())
            .orElseGet(() -> characterLocationRepository.save(template));
    }

    @Transactional(readOnly = true)
    public List<Map<String, Object>> buildSkills(UUID characterId) {
        List<CharacterSkillEntity> skills = characterSkillRepository.findByCharacterIdOrderByLevelDesc(characterId);
        if (skills.isEmpty()) {
            return Collections.emptyList();
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (CharacterSkillEntity skill : skills) {
            Map<String, Object> dto = new HashMap<>();
            dto.put("skill_id", skill.getSkillId());
            dto.put("level", skill.getLevel());
            dto.put("experience", skill.getExperience());
            result.add(dto);
        }
        return result;
    }

    public Map<String, Integer> buildAttributes(CharacterStatsEntity stats) {
        if (stats == null) {
            return Collections.emptyMap();
        }
        Map<String, Integer> attributes = new HashMap<>();
        attributes.put("strength", stats.getStrength());
        attributes.put("reflexes", stats.getReflexes());
        attributes.put("intelligence", stats.getIntelligence());
        attributes.put("technical", stats.getTechnical());
        attributes.put("cool", stats.getCool());
        return attributes;
    }

    public void persistSnapshot(UUID characterId,
                                CharacterSlotService slotService,
                                CharacterAppearanceEntity appearance,
                                CharacterEntity character,
                                CharacterStatusEntity status,
                                CharacterStatsEntity stats) {
        CharacterStatsSnapshotEntity snapshot = characterStatsSnapshotRepository.findByCharacterId(characterId)
            .orElseGet(CharacterStatsSnapshotEntity::new);
        snapshot.setCharacterId(characterId);
        slotService.findSlotByCharacterId(characterId)
            .ifPresent(slot -> snapshot.setSlotNumber(slot.getId().getSlotNumber()));
        snapshot.setPayload(writeJson(buildSnapshotPayload(character, appearance, status, stats)));
        characterStatsSnapshotRepository.save(snapshot);
    }

    public void applySnapshot(UUID characterId, String payload) {
        if (payload == null || payload.isBlank()) {
            return;
        }
        try {
            ObjectNode node = (ObjectNode) objectMapper.readTree(payload);
            ObjectNode statusNode = (ObjectNode) node.path("status");
            if (!statusNode.isMissingNode()) {
                CharacterStatusEntity status = ensureStatus(characterId);
                status.setLevel(statusNode.path("level").asInt(status.getLevel()));
                status.setExperience(statusNode.path("experience").asInt(status.getExperience()));
                status.setNextLevelExperience(statusNode.path("next_level_experience").asInt(status.getNextLevelExperience()));
                characterStatusRepository.save(status);
            }
            ObjectNode attributesNode = (ObjectNode) node.path("attributes");
            if (!attributesNode.isMissingNode()) {
                CharacterStatsEntity stats = ensureStats(characterId);
                stats.setStrength(attributesNode.path("strength").asInt(stats.getStrength()));
                stats.setReflexes(attributesNode.path("reflexes").asInt(stats.getReflexes()));
                stats.setIntelligence(attributesNode.path("intelligence").asInt(stats.getIntelligence()));
                stats.setTechnical(attributesNode.path("technical").asInt(stats.getTechnical()));
                stats.setCool(attributesNode.path("cool").asInt(stats.getCool()));
                characterStatsRepository.save(stats);
            }
        } catch (JsonProcessingException ex) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Не удалось восстановить данные персонажа");
        }
    }

    public void deleteSnapshot(UUID characterId) {
        characterStatsSnapshotRepository.findByCharacterId(characterId)
            .ifPresent(characterStatsSnapshotRepository::delete);
    }

    public CharacterStatsSnapshotEntity getSnapshot(UUID characterId) {
        return characterStatsSnapshotRepository.findByCharacterId(characterId)
            .orElseThrow(() -> new BusinessException(ErrorCode.RESOURCE_NOT_FOUND, "Не найдено резервной копии персонажа"));
    }

    private Map<String, Object> buildSnapshotPayload(CharacterEntity character,
                                                     CharacterAppearanceEntity appearance,
                                                     CharacterStatusEntity status,
                                                     CharacterStatsEntity stats) {
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> statusMap = new HashMap<>();
        statusMap.put("level", status.getLevel());
        statusMap.put("experience", status.getExperience());
        statusMap.put("next_level_experience", status.getNextLevelExperience());
        payload.put("status", statusMap);
        if (stats != null) {
            payload.put("attributes", buildAttributes(stats));
        }
        payload.put("appearance", characterAppearanceMapper.toDto(appearance));
        payload.put("city_id", character.getCity() != null ? character.getCity().getId().toString() : null);
        return payload;
    }

    public GameCharacterAppearance toAppearanceDto(CharacterAppearanceEntity appearance) {
        return appearance != null ? characterAppearanceMapper.toDto(appearance) : null;
    }

    private String writeJson(Map<String, Object> payload) {
        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException ex) {
            throw new BusinessException(ErrorCode.INTERNAL_ERROR, "Не удалось сохранить резервные данные персонажа");
        }
    }
}

