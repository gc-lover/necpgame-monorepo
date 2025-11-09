package com.necpgame.backjava.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.CharacterEntity;
import com.necpgame.backjava.entity.CharacterMilestoneEntity;
import com.necpgame.backjava.entity.CharacterProgressionEntity;
import com.necpgame.backjava.entity.CharacterStatsEntity;
import com.necpgame.backjava.entity.ProgressionMilestoneEntity;
import com.necpgame.backjava.entity.SkillEntity;
import com.necpgame.backjava.entity.SkillExperienceEntity;
import com.necpgame.backjava.model.AddSkillExperienceRequest;
import com.necpgame.backjava.model.Attribute;
import com.necpgame.backjava.model.AwardExperienceRequest;
import com.necpgame.backjava.model.CharacterAttributes;
import com.necpgame.backjava.model.CharacterAttributesAttributes;
import com.necpgame.backjava.model.CharacterExperience;
import com.necpgame.backjava.model.CharacterSkills;
import com.necpgame.backjava.model.ExperienceAwardResult;
import com.necpgame.backjava.model.GetProgressionMilestones200Response;
import com.necpgame.backjava.model.LevelUpResult;
import com.necpgame.backjava.model.LevelUpResultUnlockedContent;
import com.necpgame.backjava.model.LevelUpRewards;
import com.necpgame.backjava.model.LevelUpRewardsCurrency;
import com.necpgame.backjava.model.ProgressionMilestone;
import com.necpgame.backjava.model.ProgressionMilestoneRequirement;
import com.necpgame.backjava.model.ProgressionMilestoneRewards;
import com.necpgame.backjava.model.Skill;
import com.necpgame.backjava.model.SkillExperienceResult;
import com.necpgame.backjava.model.SkillExperienceResultRewards;
import com.necpgame.backjava.model.SpendAttributePointsRequest;
import com.necpgame.backjava.repository.CharacterMilestoneRepository;
import com.necpgame.backjava.repository.CharacterProgressionRepository;
import com.necpgame.backjava.repository.CharacterRepository;
import com.necpgame.backjava.repository.CharacterStatsRepository;
import com.necpgame.backjava.repository.ProgressionMilestoneRepository;
import com.necpgame.backjava.repository.SkillExperienceRepository;
import com.necpgame.backjava.repository.SkillRepository;
import com.necpgame.backjava.service.ProgressionBackendService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ProgressionBackendServiceImpl implements ProgressionBackendService {

    private static final long BASE_LEVEL_EXPERIENCE = 1000L;
    private static final double LEVEL_GROWTH = 1.5;
    private static final int LEVEL_CAP = 100;
    private static final int ATTRIBUTE_MAX_VALUE = 20;
    private static final int SKILL_LEVEL_CAP = 100;
    private static final int MINIMUM_SKILL_EXPERIENCE = 100;
    private static final double SKILL_GROWTH = 1.35;
    private static final TypeReference<List<Object>> LIST_TYPE = new TypeReference<>() {};

    private final CharacterRepository characterRepository;
    private final CharacterStatsRepository characterStatsRepository;
    private final CharacterProgressionRepository characterProgressionRepository;
    private final SkillRepository skillRepository;
    private final SkillExperienceRepository skillExperienceRepository;
    private final ProgressionMilestoneRepository progressionMilestoneRepository;
    private final CharacterMilestoneRepository characterMilestoneRepository;
    private final ObjectMapper objectMapper;

    @Override
    public SkillExperienceResult addSkillExperience(UUID characterId, String skillId, AddSkillExperienceRequest request) {
        CharacterProgressionEntity progression = getOrCreateProgression(characterId);
        SkillExperienceEntity experienceEntity = getOrCreateSkillExperience(characterId, skillId);
        int amount = request != null && request.getExperience() != null ? request.getExperience() : 0;
        if (amount <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "experience must be greater than zero");
        }
        int previousLevel = experienceEntity.getCurrentLevel();
        experienceEntity.setExperience(experienceEntity.getExperience() + amount);
        boolean leveledUp = false;
        List<String> unlockedAbilities = new ArrayList<>();
        int perkPoints = 0;
        while (experienceEntity.getExperience() >= experienceEntity.getExperienceToNextLevel() && experienceEntity.getCurrentLevel() < SKILL_LEVEL_CAP) {
            leveledUp = true;
            experienceEntity.setExperience(experienceEntity.getExperience() - experienceEntity.getExperienceToNextLevel());
            experienceEntity.setCurrentLevel(experienceEntity.getCurrentLevel() + 1);
            experienceEntity.setExperienceToNextLevel(calculateSkillExperienceRequirement(experienceEntity.getCurrentLevel() + 1));
            if (experienceEntity.getCurrentLevel() % 5 == 0) {
                perkPoints += 1;
            }
            if (experienceEntity.getCurrentLevel() % 10 == 0) {
                unlockedAbilities.add(skillId + "_ability_" + experienceEntity.getCurrentLevel());
            }
        }
        skillExperienceRepository.save(experienceEntity);
        refreshMilestones(characterId, progression);
        SkillExperienceResult result = new SkillExperienceResult()
            .skillId(skillId)
            .experienceAdded(amount)
            .previousLevel(previousLevel)
            .newLevel(experienceEntity.getCurrentLevel())
            .leveledUp(leveledUp);
        if (leveledUp && (perkPoints > 0 || !unlockedAbilities.isEmpty())) {
            SkillExperienceResultRewards rewards = new SkillExperienceResultRewards();
            if (perkPoints > 0) {
                rewards.setPerkPoints(perkPoints);
            }
            if (!unlockedAbilities.isEmpty()) {
                rewards.setUnlockedAbilities(unlockedAbilities);
            }
            result.setRewards(JsonNullable.of(rewards));
        }
        return result;
    }

    @Override
    public ExperienceAwardResult awardExperience(UUID characterId, AwardExperienceRequest request) {
        if (request == null || request.getAmount() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount is required");
        }
        if (request.getAmount() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "amount must be non-negative");
        }
        double multiplier = request.getMultiplier() != null ? request.getMultiplier() : 1.0;
        long awarded = Math.round(request.getAmount() * Math.max(multiplier, 0.0));
        CharacterProgressionEntity progression = getOrCreateProgression(characterId);
        CharacterEntity character = requireCharacter(characterId);
        int previousLevel = progression.getLevel();
        progression.setExperience(progression.getExperience() + awarded);
        progression.setTotalExperienceEarned(progression.getTotalExperienceEarned() + awarded);
        LevelUpRewards aggregatedRewards = null;
        boolean leveledUp = false;
        while (progression.getExperience() >= progression.getExperienceToNextLevel() && progression.getLevel() < progression.getLevelCap()) {
            leveledUp = true;
            progression.setExperience(progression.getExperience() - progression.getExperienceToNextLevel());
            progression.setLevel(progression.getLevel() + 1);
            progression.setExperienceToNextLevel(calculateLevelExperienceRequirement(progression.getLevel() + 1));
            progression.setUnspentAttributePoints(progression.getUnspentAttributePoints() + 1);
            progression.setUnspentSkillPoints(progression.getUnspentSkillPoints() + 2);
            if (aggregatedRewards == null) {
                aggregatedRewards = new LevelUpRewards();
            }
            aggregatedRewards.setAttributePoints((aggregatedRewards.getAttributePoints() == null ? 0 : aggregatedRewards.getAttributePoints()) + 1);
            aggregatedRewards.setSkillPoints((aggregatedRewards.getSkillPoints() == null ? 0 : aggregatedRewards.getSkillPoints()) + 2);
        }
        if (leveledUp) {
            character.setLevel(progression.getLevel());
            characterRepository.save(character);
        }
        progression = characterProgressionRepository.save(progression);
        refreshMilestones(characterId, progression);
        long cappedExperience = Math.min((long) Integer.MAX_VALUE, progression.getExperience());
        return new ExperienceAwardResult()
            .experienceAwarded(Math.toIntExact(awarded))
            .previousLevel(previousLevel)
            .newLevel(progression.getLevel())
            .leveledUp(leveledUp)
            .newExperienceTotal((int) cappedExperience)
            .levelUpRewards(aggregatedRewards);
    }

    @Override
    @Transactional(readOnly = true)
    public CharacterAttributes getCharacterAttributes(UUID characterId) {
        CharacterProgressionEntity progression = getOrCreateProgression(characterId);
        CharacterStatsEntity stats = characterStatsRepository.findByCharacterId(characterId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "character stats not found"));
        CharacterAttributesAttributes attributesMap = new CharacterAttributesAttributes();
        attributesMap.setBODY(buildAttribute(stats.getStrength()));
        attributesMap.setREFLEXES(buildAttribute(stats.getReflexes()));
        attributesMap.setINTELLIGENCE(buildAttribute(stats.getIntelligence()));
        attributesMap.setTECHNICALABILITY(buildAttribute(stats.getTechnical()));
        attributesMap.setCOOL(buildAttribute(stats.getCool()));
        return new CharacterAttributes()
            .characterId(characterId)
            .unspentPoints(progression.getUnspentAttributePoints())
            .attributes(attributesMap);
    }

    @Override
    @Transactional(readOnly = true)
    public CharacterExperience getCharacterExperience(UUID characterId) {
        CharacterProgressionEntity progression = getOrCreateProgression(characterId);
        double progress = progression.getExperienceToNextLevel() == 0
            ? 100.0
            : BigDecimal.valueOf(progression.getExperience())
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(progression.getExperienceToNextLevel()), 2, RoundingMode.HALF_UP)
                .doubleValue();
        long cappedExperience = Math.min((long) Integer.MAX_VALUE, progression.getExperience());
        long cappedNext = Math.min((long) Integer.MAX_VALUE, progression.getExperienceToNextLevel());
        long cappedTotal = Math.min((long) Integer.MAX_VALUE, progression.getTotalExperienceEarned());
        return new CharacterExperience()
            .characterId(characterId)
            .level(progression.getLevel())
            .experience((int) cappedExperience)
            .experienceToNextLevel((int) cappedNext)
            .progressToNextLevel((float) progress)
            .totalExperienceEarned((int) cappedTotal)
            .levelCap(progression.getLevelCap());
    }

    @Override
    @Transactional(readOnly = true)
    public CharacterSkills getCharacterSkills(UUID characterId) {
        getOrCreateProgression(characterId);
        List<SkillExperienceEntity> experiences = skillExperienceRepository.findByCharacterIdOrderByCurrentLevelDescExperienceDesc(characterId);
        List<Skill> skills = new ArrayList<>();
        for (SkillExperienceEntity entity : experiences) {
            Skill skill = new Skill()
                .skillId(entity.getSkillId())
                .level(entity.getCurrentLevel())
                .experience(entity.getExperience())
                .experienceToNextLevel(entity.getExperienceToNextLevel());
            double progress = entity.getExperienceToNextLevel() == 0
                ? 100.0
                : BigDecimal.valueOf(entity.getExperience())
                    .multiply(BigDecimal.valueOf(100))
                    .divide(BigDecimal.valueOf(entity.getExperienceToNextLevel()), 2, RoundingMode.HALF_UP)
                    .doubleValue();
            skill.setProgressPercentage((float) progress);
            SkillEntity definition = skillRepository.findById(entity.getSkillId()).orElse(null);
            if (definition != null) {
                skill.setName(definition.getName());
                skill.setAttributeDependency(resolveAttributeDependency(definition));
            } else {
                skill.setName(entity.getSkillId());
                skill.setAttributeDependency("BODY");
            }
            skills.add(skill);
        }
        return new CharacterSkills()
            .characterId(characterId)
            .skills(skills);
    }

    @Override
    public GetProgressionMilestones200Response getProgressionMilestones(UUID characterId) {
        CharacterProgressionEntity progression = getOrCreateProgression(characterId);
        refreshMilestones(characterId, progression);
        List<ProgressionMilestone> milestoneDtos = new ArrayList<>();
        List<CharacterMilestoneEntity> completed = characterMilestoneRepository.findByCharacterId(characterId);
        Set<UUID> completedIds = new HashSet<>();
        for (CharacterMilestoneEntity entity : completed) {
            completedIds.add(entity.getMilestoneId());
        }
        List<SkillExperienceEntity> skills = skillExperienceRepository.findByCharacterIdOrderByCurrentLevelDescExperienceDesc(characterId);
        int maxSkillLevel = skills.isEmpty() ? 0 : skills.get(0).getCurrentLevel();
        for (ProgressionMilestoneEntity milestone : progressionMilestoneRepository.findAll()) {
            ProgressionMilestone dto = new ProgressionMilestone()
                .milestoneId(milestone.getId().toString())
                .name(milestone.getName())
                .description(milestone.getDescription());
            ProgressionMilestoneRequirement requirement = new ProgressionMilestoneRequirement()
                .type(ProgressionMilestoneRequirement.TypeEnum.valueOf(milestone.getRequirementType().name()))
                .targetValue(milestone.getTargetValue());
            dto.setRequirement(requirement);
            boolean completedFlag = completedIds.contains(milestone.getId());
            if (!completedFlag) {
                completedFlag = isMilestoneSatisfied(milestone, progression, maxSkillLevel);
            }
            dto.setCompleted(completedFlag);
            dto.setRewards(buildMilestoneRewards(milestone));
            milestoneDtos.add(dto);
        }
        return new GetProgressionMilestones200Response().milestones(milestoneDtos);
    }

    @Override
    public LevelUpResult levelUp(UUID characterId) {
        CharacterProgressionEntity progression = getOrCreateProgression(characterId);
        if (progression.getExperience() < progression.getExperienceToNextLevel()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "insufficient experience for level up");
        }
        if (progression.getLevel() >= progression.getLevelCap()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "level cap reached");
        }
        int previousLevel = progression.getLevel();
        progression.setExperience(progression.getExperience() - progression.getExperienceToNextLevel());
        progression.setLevel(progression.getLevel() + 1);
        progression.setExperienceToNextLevel(calculateLevelExperienceRequirement(progression.getLevel() + 1));
        progression.setUnspentAttributePoints(progression.getUnspentAttributePoints() + 1);
        progression.setUnspentSkillPoints(progression.getUnspentSkillPoints() + 2);
        LevelUpRewards rewards = new LevelUpRewards()
            .attributePoints(1)
            .skillPoints(2)
            .perkPoints(0);
        progression = characterProgressionRepository.save(progression);
        CharacterEntity character = requireCharacter(characterId);
        character.setLevel(progression.getLevel());
        characterRepository.save(character);
        refreshMilestones(characterId, progression);
        LevelUpResultUnlockedContent unlocked = new LevelUpResultUnlockedContent();
        unlocked.setQuests(new ArrayList<>());
        unlocked.setLocations(new ArrayList<>());
        unlocked.setAbilities(new ArrayList<>());
        return new LevelUpResult()
            .characterId(characterId)
            .previousLevel(previousLevel)
            .newLevel(progression.getLevel())
            .rewards(rewards)
            .unlockedContent(unlocked);
    }

    @Override
    public CharacterAttributes spendAttributePoints(UUID characterId, SpendAttributePointsRequest request) {
        if (request == null || CollectionUtils.isEmpty(request.getDistributions())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "distributions are required");
        }
        CharacterProgressionEntity progression = getOrCreateProgression(characterId);
        Map<String, Integer> distributions = request.getDistributions();
        int totalRequested = distributions.values().stream().mapToInt(Integer::intValue).sum();
        if (totalRequested <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "distribution must contain positive values");
        }
        if (totalRequested > progression.getUnspentAttributePoints()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "not enough unspent attribute points");
        }
        CharacterStatsEntity stats = characterStatsRepository.findByCharacterId(characterId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "character stats not found"));
        for (Map.Entry<String, Integer> entry : distributions.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            if (!StringUtils.hasText(key) || value == null || value <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid distribution entry");
            }
            switch (key.toUpperCase()) {
                case "BODY" -> stats.setStrength(applyAttributeIncrease(stats.getStrength(), value));
                case "REFLEXES" -> stats.setReflexes(applyAttributeIncrease(stats.getReflexes(), value));
                case "INTELLIGENCE" -> stats.setIntelligence(applyAttributeIncrease(stats.getIntelligence(), value));
                case "TECHNICAL_ABILITY" -> stats.setTechnical(applyAttributeIncrease(stats.getTechnical(), value));
                case "COOL" -> stats.setCool(applyAttributeIncrease(stats.getCool(), value));
                default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unknown attribute " + key);
            }
        }
        progression.setUnspentAttributePoints(progression.getUnspentAttributePoints() - totalRequested);
        progression.setTotalAttributePointsSpent(progression.getTotalAttributePointsSpent() + totalRequested);
        characterStatsRepository.save(stats);
        characterProgressionRepository.save(progression);
        return getCharacterAttributes(characterId);
    }

    private CharacterProgressionEntity getOrCreateProgression(UUID characterId) {
        return characterProgressionRepository.findById(characterId).orElseGet(() -> {
            requireCharacter(characterId);
            CharacterProgressionEntity entity = new CharacterProgressionEntity();
            entity.setCharacterId(characterId);
            entity.setLevel(1);
            entity.setExperience(0L);
            entity.setExperienceToNextLevel(calculateLevelExperienceRequirement(2));
            entity.setLevelCap(LEVEL_CAP);
            return characterProgressionRepository.save(entity);
        });
    }

    private SkillExperienceEntity getOrCreateSkillExperience(UUID characterId, String skillId) {
        SkillEntity skill = skillRepository.findById(skillId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "skill not found"));
        return skillExperienceRepository.findByCharacterIdAndSkillId(characterId, skillId).orElseGet(() -> {
            SkillExperienceEntity entity = new SkillExperienceEntity();
            entity.setCharacterId(characterId);
            entity.setSkillId(skillId);
            entity.setCurrentLevel(0);
            entity.setExperience(0);
            entity.setExperienceToNextLevel(calculateSkillExperienceRequirement(1));
            entity.setSkill(skill);
            return skillExperienceRepository.save(entity);
        });
    }

    private CharacterEntity requireCharacter(UUID characterId) {
        return characterRepository.findById(characterId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "character not found"));
    }

    private int applyAttributeIncrease(Integer current, int increment) {
        int safeCurrent = current == null ? 0 : current;
        int result = safeCurrent + increment;
        if (result > ATTRIBUTE_MAX_VALUE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "attribute cap reached");
        }
        return result;
    }

    private Attribute buildAttribute(Integer value) {
        int safeValue = value == null ? 0 : value;
        return new Attribute()
            .value(safeValue)
            .baseValue(safeValue)
            .modifier(0)
            .maxValue(ATTRIBUTE_MAX_VALUE);
    }

    private long calculateLevelExperienceRequirement(int level) {
        int safeLevel = Math.max(level, 2);
        double exponent = Math.max(safeLevel - 2, 0);
        long requirement = Math.round(BASE_LEVEL_EXPERIENCE * Math.pow(LEVEL_GROWTH, exponent));
        return Math.max(requirement, BASE_LEVEL_EXPERIENCE);
    }

    private int calculateSkillExperienceRequirement(int level) {
        int safeLevel = Math.max(level, 1);
        double exponent = Math.max(safeLevel - 1, 0);
        int requirement = (int) Math.round(MINIMUM_SKILL_EXPERIENCE * Math.pow(SKILL_GROWTH, exponent));
        return Math.max(requirement, MINIMUM_SKILL_EXPERIENCE);
    }

    private void refreshMilestones(UUID characterId, CharacterProgressionEntity progression) {
        List<ProgressionMilestoneEntity> milestones = progressionMilestoneRepository.findAll();
        if (milestones.isEmpty()) {
            return;
        }
        List<SkillExperienceEntity> skills = skillExperienceRepository.findByCharacterIdOrderByCurrentLevelDescExperienceDesc(characterId);
        int highestSkillLevel = skills.isEmpty() ? 0 : skills.get(0).getCurrentLevel();
        Set<UUID> existing = new HashSet<>();
        for (CharacterMilestoneEntity completed : characterMilestoneRepository.findByCharacterId(characterId)) {
            existing.add(completed.getMilestoneId());
        }
        for (ProgressionMilestoneEntity milestone : milestones) {
            if (existing.contains(milestone.getId())) {
                continue;
            }
            if (isMilestoneSatisfied(milestone, progression, highestSkillLevel)) {
                CharacterMilestoneEntity entity = new CharacterMilestoneEntity();
                entity.setCharacterId(characterId);
                entity.setMilestoneId(milestone.getId());
                characterMilestoneRepository.save(entity);
            }
        }
    }

    private boolean isMilestoneSatisfied(ProgressionMilestoneEntity milestone, CharacterProgressionEntity progression, int highestSkillLevel) {
        return switch (milestone.getRequirementType()) {
            case LEVEL -> progression.getLevel() >= milestone.getTargetValue();
            case TOTAL_EXPERIENCE -> progression.getTotalExperienceEarned() >= milestone.getTargetValue();
            case SKILL_LEVEL -> highestSkillLevel >= milestone.getTargetValue();
        };
    }

    private ProgressionMilestoneRewards buildMilestoneRewards(ProgressionMilestoneEntity milestone) {
        if (!StringUtils.hasText(milestone.getRewardTitle()) && !StringUtils.hasText(milestone.getRewardsJson())) {
            return null;
        }
        ProgressionMilestoneRewards rewards = new ProgressionMilestoneRewards();
        rewards.setTitle(milestone.getRewardTitle());
        if (StringUtils.hasText(milestone.getRewardsJson())) {
            try {
                rewards.setItems(objectMapper.readValue(milestone.getRewardsJson(), LIST_TYPE));
            } catch (JsonProcessingException ex) {
                log.warn("Unable to parse rewards for milestone {}: {}", milestone.getId(), ex.getMessage());
                rewards.setItems(new ArrayList<>());
            }
        }
        return rewards;
    }

    private String resolveAttributeDependency(SkillEntity skill) {
        if (!StringUtils.hasText(skill.getCategory())) {
            return "BODY";
        }
        return switch (skill.getCategory().toLowerCase()) {
            case "technical" -> "TECHNICAL_ABILITY";
            case "intelligence" -> "INTELLIGENCE";
            case "stealth" -> "COOL";
            case "reflex" -> "REFLEXES";
            default -> "BODY";
        };
    }
}


