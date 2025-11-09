package com.necpgame.backjava.service.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.CharacterFactionQuestProgressEntity;
import com.necpgame.backjava.entity.CharacterFactionQuestProgressEntity.ProgressStatus;
import com.necpgame.backjava.entity.FactionQuestBranchEntity;
import com.necpgame.backjava.entity.FactionQuestEndingEntity;
import com.necpgame.backjava.entity.FactionQuestEntity;
import com.necpgame.backjava.entity.QuestCatalogEntryEntity;
import com.necpgame.backjava.entity.QuestDialogueStateEntity;
import com.necpgame.backjava.entity.QuestEntity;
import com.necpgame.backjava.entity.QuestInstanceEntity;
import com.necpgame.backjava.entity.QuestInstanceEntity.QuestStatus;
import com.necpgame.backjava.entity.QuestObjectiveEntity;
import com.necpgame.backjava.entity.QuestObjectiveEntity.ObjectiveType;
import com.necpgame.backjava.entity.QuestSkillCheckResultEntity;
import com.necpgame.backjava.model.DialogueTree;
import com.necpgame.backjava.model.ActiveQuestProgress;
import com.necpgame.backjava.model.ActiveQuestProgressChoicesMadeInner;
import com.necpgame.backjava.model.ActiveQuestProgressObjectivesInner;
import com.necpgame.backjava.model.CompleteQuestRequest;
import com.necpgame.backjava.model.DialogueChoiceRequest;
import com.necpgame.backjava.model.DialogueChoiceResult;
import com.necpgame.backjava.model.DialogueNode;
import com.necpgame.backjava.model.DialogueOption;
import com.necpgame.backjava.model.GetQuestCatalog200Response;
import com.necpgame.backjava.model.FactionQuest;
import com.necpgame.backjava.model.FactionQuest.FactionEnum;
import com.necpgame.backjava.model.FactionQuest.DifficultyEnum;
import com.necpgame.backjava.model.FactionQuestDetailed;
import com.necpgame.backjava.model.FactionQuestDetailedAllOfKeyNpcs;
import com.necpgame.backjava.model.GetActiveQuests200Response;
import com.necpgame.backjava.model.GetAvailableFactionQuests200Response;
import com.necpgame.backjava.model.GetAvailableFactionQuests200ResponseLockedQuestsInner;
import com.necpgame.backjava.model.GetFactionQuestProgress200Response;
import com.necpgame.backjava.model.GetFactionQuestProgress200ResponseCompletedQuestsInner;
import com.necpgame.backjava.model.GetQuestBranches200Response;
import com.necpgame.backjava.model.GetQuestChains200Response;
import com.necpgame.backjava.model.GetQuestEndings200Response;
import com.necpgame.backjava.model.GetQuestRecommendations200Response;
import com.necpgame.backjava.model.GetQuestRecommendations200ResponseRecommendationsInner;
import com.necpgame.backjava.model.ListFactionQuests200Response;
import com.necpgame.backjava.model.PaginationMeta;
import com.necpgame.backjava.model.QuestCatalogEntry;
import com.necpgame.backjava.model.QuestCatalogEntryRewardsSummary;
import com.necpgame.backjava.model.QuestChain;
import com.necpgame.backjava.model.QuestChainQuestsInner;
import com.necpgame.backjava.model.QuestBranch;
import com.necpgame.backjava.model.QuestCompletionResult;
import com.necpgame.backjava.model.QuestEnding;
import com.necpgame.backjava.model.QuestInstance;
import com.necpgame.backjava.model.QuestInstanceProgressValue;
import com.necpgame.backjava.model.QuestObjective;
import com.necpgame.backjava.model.QuestProgress;
import com.necpgame.backjava.model.QuestRequirements;
import com.necpgame.backjava.model.QuestRewards;
import com.necpgame.backjava.model.QuestRewardsItemsInner;
import com.necpgame.backjava.model.QuestDetails;
import com.necpgame.backjava.model.QuestDetailsAllOfKeyNpcs;
import com.necpgame.backjava.model.QuestDetailsAllOfObjectives;
import com.necpgame.backjava.model.QuestDetailsAllOfRewardsDetailed;
import com.necpgame.backjava.model.QuestLootTable;
import com.necpgame.backjava.model.QuestLootTableGuaranteedLootInner;
import com.necpgame.backjava.model.QuestLootTableRandomLootInner;
import com.necpgame.backjava.model.QuestLootTableRandomLootInnerQuantityRange;
import com.necpgame.backjava.model.QuestSearchResult;
import com.necpgame.backjava.model.SearchQuests200Response;
import com.necpgame.backjava.model.SkillCheckRequest;
import com.necpgame.backjava.model.SkillCheckResult;
import com.necpgame.backjava.model.StartQuestRequest;
import com.necpgame.backjava.repository.CharacterFactionQuestProgressRepository;
import com.necpgame.backjava.repository.CharacterProgressionRepository;
import com.necpgame.backjava.repository.FactionQuestBranchRepository;
import com.necpgame.backjava.repository.FactionQuestEndingRepository;
import com.necpgame.backjava.repository.FactionQuestRepository;
import com.necpgame.backjava.repository.QuestCatalogEntryRepository;
import com.necpgame.backjava.repository.QuestDialogueStateRepository;
import com.necpgame.backjava.repository.QuestInstanceRepository;
import com.necpgame.backjava.repository.QuestObjectiveRepository;
import com.necpgame.backjava.repository.QuestRepository;
import com.necpgame.backjava.repository.QuestSkillCheckResultRepository;
import com.necpgame.backjava.repository.QuestTemplateDefinitionRepository;
import com.necpgame.backjava.service.NarrativeService;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NarrativeServiceImpl implements NarrativeService {

    private static final TypeReference<Map<String, ProgressSnapshot>> PROGRESS_TYPE = new TypeReference<>() {};
    private static final TypeReference<Map<String, Object>> FLAGS_TYPE = new TypeReference<>() {};
    private static final TypeReference<List<String>> STRING_LIST_TYPE = new TypeReference<>() {};
    private static final TypeReference<List<ChoiceRecord>> CHOICE_LIST_TYPE = new TypeReference<>() {};
    private static final TypeReference<List<FactionQuestDetailedAllOfKeyNpcs>> KEY_NPC_LIST_TYPE = new TypeReference<>() {};
    private static final TypeReference<List<ActiveQuestProgressChoicesMadeInner>> PROGRESS_CHOICES_LIST_TYPE = new TypeReference<>() {};
    private static final TypeReference<List<ActiveQuestProgressObjectivesInner>> PROGRESS_OBJECTIVES_LIST_TYPE = new TypeReference<>() {};
    private static final TypeReference<List<QuestDetailsAllOfObjectives>> QUEST_DETAILS_OBJECTIVES_TYPE = new TypeReference<>() {};
    private static final TypeReference<List<QuestDetailsAllOfKeyNpcs>> QUEST_DETAILS_KEY_NPCS_TYPE = new TypeReference<>() {};
    private static final TypeReference<QuestDetailsAllOfRewardsDetailed> QUEST_DETAILS_REWARDS_TYPE = new TypeReference<>() {};
    private static final TypeReference<Map<String, Integer>> REWARDS_SUMMARY_MAP_TYPE = new TypeReference<>() {};
    private static final TypeReference<Map<String, Object>> STRING_OBJECT_MAP_TYPE = new TypeReference<>() {};

    private final QuestRepository questRepository;
    private final QuestObjectiveRepository questObjectiveRepository;
    private final QuestInstanceRepository questInstanceRepository;
    private final QuestDialogueStateRepository dialogueStateRepository;
    private final QuestSkillCheckResultRepository skillCheckResultRepository;
    private final QuestTemplateDefinitionRepository templateDefinitionRepository;
    private final FactionQuestRepository factionQuestRepository;
    private final FactionQuestBranchRepository factionQuestBranchRepository;
    private final FactionQuestEndingRepository factionQuestEndingRepository;
    private final QuestCatalogEntryRepository questCatalogEntryRepository;
    private final CharacterFactionQuestProgressRepository characterFactionQuestProgressRepository;
    private final CharacterProgressionRepository characterProgressionRepository;
    private final ObjectMapper objectMapper;

    @Value("${quest.engine.max-active-quests:20}")
    private int maxActiveQuests;

    @Override
    @Transactional(readOnly = true)
    public GetQuestCatalog200Response getQuestCatalog(@Nullable String type, @Nullable String period, @Nullable String difficulty, @Nullable String faction, @Nullable Integer minLevel, @Nullable Integer maxLevel, @Nullable Boolean hasRomance, @Nullable Boolean hasCombat, @Nullable Integer estimatedTimeMin, @Nullable Integer estimatedTimeMax, @Nullable Integer page, @Nullable Integer pageSize) {
        Specification<QuestCatalogEntryEntity> specification = buildQuestCatalogSpecification(type, period, difficulty, faction, minLevel, maxLevel, hasRomance, hasCombat, estimatedTimeMin, estimatedTimeMax);
        Pageable pageable = createPageable(page, pageSize);
        Page<QuestCatalogEntryEntity> quests = questCatalogEntryRepository.findAll(specification, pageable);

        List<QuestCatalogEntry> data = quests.getContent().stream()
            .map(this::toQuestCatalogEntry)
            .collect(Collectors.toList());

        PaginationMeta meta = new PaginationMeta()
            .page(quests.getNumber() + 1)
            .pageSize(quests.getSize())
            .total(safeLongToInt(quests.getTotalElements()))
            .totalPages(quests.getTotalPages())
            .hasNext(quests.hasNext())
            .hasPrev(quests.hasPrevious());

        Map<String, Object> filters = new LinkedHashMap<>();
        if (StringUtils.hasText(type)) {
            filters.put("type", type);
        }
        if (StringUtils.hasText(period)) {
            filters.put("period", period);
        }
        if (StringUtils.hasText(difficulty)) {
            filters.put("difficulty", difficulty);
        }
        if (StringUtils.hasText(faction)) {
            filters.put("faction", faction);
        }
        if (minLevel != null) {
            filters.put("min_level", minLevel);
        }
        if (maxLevel != null) {
            filters.put("max_level", maxLevel);
        }
        if (hasRomance != null) {
            filters.put("has_romance", hasRomance);
        }
        if (hasCombat != null) {
            filters.put("has_combat", hasCombat);
        }
        if (estimatedTimeMin != null) {
            filters.put("estimated_time_min", estimatedTimeMin);
        }
        if (estimatedTimeMax != null) {
            filters.put("estimated_time_max", estimatedTimeMax);
        }

        Object filtersApplied = filters.isEmpty() ? null : filters;

        return new GetQuestCatalog200Response()
            .data(data)
            .meta(meta)
            .filtersApplied(filtersApplied);
    }

    @Override
    @Transactional(readOnly = true)
    public SearchQuests200Response searchQuests(String query, @Nullable List<String> searchIn, @Nullable Integer page, @Nullable Integer pageSize) {
        String safeQuery = requireText(query, "q");
        Specification<QuestCatalogEntryEntity> specification = buildQuestCatalogSearchSpecification(safeQuery, searchIn);
        Pageable pageable = createPageable(page, pageSize);
        Page<QuestCatalogEntryEntity> quests = questCatalogEntryRepository.findAll(specification, pageable);

        List<QuestSearchResult> results = quests.getContent().stream()
            .map(entity -> toQuestSearchResult(entity, safeQuery))
            .collect(Collectors.toList());

        PaginationMeta meta = new PaginationMeta()
            .page(quests.getNumber() + 1)
            .pageSize(quests.getSize())
            .total(safeLongToInt(quests.getTotalElements()))
            .totalPages(quests.getTotalPages())
            .hasNext(quests.hasNext())
            .hasPrev(quests.hasPrevious());

        return new SearchQuests200Response()
            .data(results)
            .meta(meta);
    }

    @Override
    @Transactional(readOnly = true)
    public QuestDetails getQuestDetails(String questId) {
        QuestCatalogEntryEntity entity = findQuestCatalogEntry(requireText(questId, "quest_id"));
        return toQuestDetails(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public DialogueTree getQuestDialogueTree(String questId) {
        QuestCatalogEntryEntity entity = findQuestCatalogEntry(requireText(questId, "quest_id"));
        if (!StringUtils.hasText(entity.getDialogueTreeJson())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "dialogue tree not available for quest");
        }
        DialogueTree tree = readValue(entity.getDialogueTreeJson(), DialogueTree.class);
        if (tree == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "dialogue tree not available for quest");
        }
        return tree;
    }

    @Override
    @Transactional(readOnly = true)
    public QuestLootTable getQuestLootTable(String questId) {
        QuestCatalogEntryEntity entity = findQuestCatalogEntry(requireText(questId, "quest_id"));
        if (!StringUtils.hasText(entity.getLootTableJson())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "loot table not available for quest");
        }
        QuestLootTable lootTable = toQuestLootTable(entity);
        if (lootTable.getGuaranteedLoot() == null && lootTable.getRandomLoot() == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "loot table not available for quest");
        }
        return lootTable;
    }

    @Override
    @Transactional(readOnly = true)
    public GetQuestRecommendations200Response getQuestRecommendations(UUID characterId, @Nullable Integer count) {
        int safeCount = count == null || count < 1 ? 10 : Math.min(count, 50);
        UUID safeCharacterId = Objects.requireNonNull(characterId, "characterId");
        int characterLevel = characterProgressionRepository.findById(safeCharacterId)
            .map(progress -> Optional.ofNullable(progress.getLevel()).orElse(1))
            .orElse(1);

        List<GetQuestRecommendations200ResponseRecommendationsInner> recommendations = questCatalogEntryRepository.findAll().stream()
            .map(entity -> buildRecommendation(entity, characterLevel))
            .sorted(Comparator.comparing(GetQuestRecommendations200ResponseRecommendationsInner::getMatchScore).reversed())
            .limit(safeCount)
            .collect(Collectors.toList());

        return new GetQuestRecommendations200Response()
            .recommendations(recommendations);
    }

    @Override
    @Transactional(readOnly = true)
    public GetQuestChains200Response getQuestChains(@Nullable String faction, @Nullable String storyline) {
        List<QuestCatalogEntryEntity> entries = questCatalogEntryRepository.findAll();
        final String factionFilter = normalizeFilter(faction);
        final String storylineFilter = normalizeFilter(storyline);

        Map<String, List<QuestCatalogEntryEntity>> grouped = entries.stream()
            .filter(entity -> StringUtils.hasText(entity.getStoryline()))
            .filter(entity -> factionFilter == null || (StringUtils.hasText(entity.getFaction()) && entity.getFaction().trim().equalsIgnoreCase(factionFilter)))
            .filter(entity -> storylineFilter == null || entity.getStoryline().trim().toLowerCase().contains(storylineFilter))
            .collect(Collectors.groupingBy(QuestCatalogEntryEntity::getStoryline, LinkedHashMap::new, Collectors.toList()));

        List<QuestChain> chains = grouped.entrySet().stream()
            .map(entry -> toQuestChain(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());

        return new GetQuestChains200Response()
            .chains(chains);
    }

    @Override
    @Transactional(readOnly = true)
    public ListFactionQuests200Response listFactionQuests(String faction, Integer minReputation, Integer playerLevelMin, Integer page, Integer pageSize) {
        Pageable pageable = createPageable(page, pageSize);
        Specification<FactionQuestEntity> specification = buildFactionQuestSpecification(faction, minReputation, playerLevelMin);
        Page<FactionQuestEntity> quests = factionQuestRepository.findAll(specification, pageable);

        List<FactionQuest> items = quests.getContent().stream()
            .map(this::toFactionQuest)
            .collect(Collectors.toList());

        PaginationMeta meta = new PaginationMeta()
            .page(quests.getNumber() + 1)
            .pageSize(quests.getSize())
            .total(safeLongToInt(quests.getTotalElements()))
            .totalPages(quests.getTotalPages())
            .hasNext(quests.hasNext())
            .hasPrev(quests.hasPrevious());

        return new ListFactionQuests200Response()
            .data(items)
            .meta(meta);
    }

    @Override
    @Transactional(readOnly = true)
    public FactionQuestDetailed getFactionQuest(String questId) {
        String safeQuestId = requireText(questId, "quest_id");
        FactionQuestEntity quest = factionQuestRepository.findById(safeQuestId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "faction quest not found"));
        List<FactionQuestBranchEntity> branches = factionQuestBranchRepository.findByQuestIdOrderByBranchId(safeQuestId);
        return toFactionQuestDetailed(quest, branches);
    }

    @Override
    @Transactional(readOnly = true)
    public GetQuestBranches200Response getQuestBranches(String questId) {
        String safeQuestId = requireText(questId, "quest_id");
        if (!factionQuestRepository.existsById(safeQuestId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "faction quest not found");
        }
        List<QuestBranch> branches = factionQuestBranchRepository.findByQuestIdOrderByBranchId(safeQuestId).stream()
            .map(this::toQuestBranch)
            .collect(Collectors.toList());
        String currentBranch = characterFactionQuestProgressRepository.findByQuestIdAndStatus(safeQuestId, ProgressStatus.ACTIVE).stream()
            .map(CharacterFactionQuestProgressEntity::getCurrentBranch)
            .filter(StringUtils::hasText)
            .findFirst()
            .orElse(null);
        return new GetQuestBranches200Response()
            .branches(branches)
            .currentBranch(currentBranch);
    }

    @Override
    @Transactional(readOnly = true)
    public GetQuestEndings200Response getQuestEndings(String questId) {
        String safeQuestId = requireText(questId, "quest_id");
        if (!factionQuestRepository.existsById(safeQuestId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "faction quest not found");
        }
        List<QuestEnding> endings = factionQuestEndingRepository.findByQuestIdOrderByEndingId(safeQuestId).stream()
            .map(this::toQuestEnding)
            .collect(Collectors.toList());
        List<String> achievedEndings = new ArrayList<>(characterFactionQuestProgressRepository.findByQuestIdAndStatus(safeQuestId, ProgressStatus.COMPLETED).stream()
            .map(CharacterFactionQuestProgressEntity::getEndingAchieved)
            .filter(StringUtils::hasText)
            .collect(Collectors.toCollection(LinkedHashSet::new)));
        return new GetQuestEndings200Response()
            .endings(endings)
            .achievedEndings(achievedEndings);
    }

    @Override
    @Transactional(readOnly = true)
    public GetAvailableFactionQuests200Response getAvailableFactionQuests(UUID characterId) {
        UUID safeCharacterId = requireId(characterId, "character_id");
        List<FactionQuestEntity> quests = factionQuestRepository.findAll();
        Map<String, CharacterFactionQuestProgressEntity> progressMap = characterFactionQuestProgressRepository.findByCharacterId(safeCharacterId).stream()
            .collect(Collectors.toMap(CharacterFactionQuestProgressEntity::getQuestId, entry -> entry));

        int characterLevel = characterProgressionRepository.findById(safeCharacterId)
            .map(entity -> entity.getLevel() == null ? 0 : entity.getLevel())
            .orElse(0);

        List<FactionQuest> available = new ArrayList<>();
        List<GetAvailableFactionQuests200ResponseLockedQuestsInner> locked = new ArrayList<>();

        for (FactionQuestEntity quest : quests) {
            FactionQuest questDto = toFactionQuest(quest);
            CharacterFactionQuestProgressEntity progress = progressMap.get(quest.getQuestId());
            boolean completed = progress != null && progress.getStatus() == ProgressStatus.COMPLETED;
            boolean meetsLevel = quest.getMinLevelRequirement() == null || quest.getMinLevelRequirement() <= characterLevel;

            if (!completed && meetsLevel) {
                available.add(questDto);
            } else {
                QuestRequirements requirements = readValue(quest.getRequirementsJson(), QuestRequirements.class);
                locked.add(new GetAvailableFactionQuests200ResponseLockedQuestsInner()
                    .quest(questDto)
                    .requirements(requirements));
            }
        }

        return new GetAvailableFactionQuests200Response()
            .availableQuests(available)
            .lockedQuests(locked);
    }

    @Override
    @Transactional(readOnly = true)
    public GetFactionQuestProgress200Response getFactionQuestProgress(UUID characterId) {
        UUID safeCharacterId = requireId(characterId, "character_id");
        List<CharacterFactionQuestProgressEntity> entries = characterFactionQuestProgressRepository.findByCharacterId(safeCharacterId);

        List<ActiveQuestProgress> active = new ArrayList<>();
        List<GetFactionQuestProgress200ResponseCompletedQuestsInner> completed = new ArrayList<>();

        for (CharacterFactionQuestProgressEntity entry : entries) {
            if (entry.getStatus() == ProgressStatus.ACTIVE) {
                active.add(toActiveQuestProgress(entry));
            } else if (entry.getStatus() == ProgressStatus.COMPLETED) {
                completed.add(toCompletedQuestProgress(entry));
            }
        }

        return new GetFactionQuestProgress200Response()
            .activeQuests(active)
            .completedQuests(completed);
    }

    @Override
    public Void abandonQuest(UUID instanceId) {
        UUID questInstanceId = requireId(instanceId, "instance_id");
        QuestInstanceEntity instance = questInstanceRepository.findById(questInstanceId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quest instance not found"));

        if (instance.getStatus() == QuestStatus.COMPLETED || instance.getStatus() == QuestStatus.FAILED) {
            return null;
        }

        instance.setStatus(QuestStatus.ABANDONED);
        instance.setCompletedAt(OffsetDateTime.now());
        questInstanceRepository.save(instance);

        dialogueStateRepository.findByQuestInstanceId(questInstanceId)
            .ifPresent(dialogueStateRepository::delete);

        return null;
    }

    @Override
    @SuppressWarnings("null")
    public DialogueChoiceResult chooseDialogueOption(UUID instanceId, DialogueChoiceRequest dialogueChoiceRequest) {
        if (dialogueChoiceRequest == null || !StringUtils.hasText(dialogueChoiceRequest.getOptionId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Option id is required");
        }

        UUID questInstanceId = requireId(instanceId, "instance_id");
        QuestInstanceEntity instance = questInstanceRepository.findById(questInstanceId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quest instance not found"));

        QuestTemplateDefinition definition = loadTemplateDefinition(requireText(instance.getQuestTemplateId(), "quest_template_id"));
        QuestDialogueStateEntity state = dialogueStateRepository.findByQuestInstanceId(questInstanceId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dialogue state not initialised"));

        DialogueNodeDefinition nodeDefinition = resolveDialogueNode(definition, state.getCurrentNodeId());
        String optionId = requireText(dialogueChoiceRequest.getOptionId(), "option_id");
        DialogueOptionDefinition optionDefinition = nodeDefinition.getOptions().stream()
            .filter(opt -> Objects.equals(opt.getOptionId(), optionId))
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dialogue option not found"));

        Map<String, ProgressSnapshot> progress = readProgress(instance.getProgressJson());
        Map<String, Object> flags = readFlags(instance.getFlagsJson());

        if (!isOptionAvailable(optionDefinition, flags)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dialogue option is not available");
        }

        SkillCheckResult performedSkillCheck = null;
        boolean skillCheckSuccess = true;
        if (optionDefinition.getSkillCheck() != null) {
            performedSkillCheck = executeSkillCheck(instance, optionDefinition.getSkillCheck(), true);
            skillCheckSuccess = Boolean.TRUE.equals(performedSkillCheck.getSuccess());
        }

        DialogueChoiceResult result = new DialogueChoiceResult()
            .skillCheckPerformed(performedSkillCheck)
            .effectsApplied(new HashMap<>())
            .questUpdated(false);

        if (!skillCheckSuccess) {
            result.success(false);
            DialogueNode currentNode = buildDialogueNode(nodeDefinition, definition, flags);
            result.nextNode(currentNode);
            return result;
        }

        boolean questUpdated = applyEffects(optionDefinition.getEffects(), progress, flags);
        String nextNodeId = StringUtils.hasText(optionDefinition.getLeadsToNode())
            ? optionDefinition.getLeadsToNode()
            : state.getCurrentNodeId();
        nextNodeId = requireText(nextNodeId, "next_node_id");

        String currentNodeId = requireText(nodeDefinition.getNodeId(), "node_id");
        String chosenOptionId = requireText(optionDefinition.getOptionId(), "option_id");
        updateDialogueState(state, currentNodeId, chosenOptionId, nextNodeId);

        instance.setCurrentBranchId(optionDefinition.getBranchIdOrDefault(definition.getStartBranchId()));
        instance.setCurrentDialogueNodeId(nextNodeId);
        instance.setProgressJson(writeProgress(progress));
        instance.setFlagsJson(writeFlags(flags));
        questInstanceRepository.save(instance);

        result.success(true);
        result.setQuestUpdated(questUpdated);
        result.setEffectsApplied(optionDefinition.getEffects() == null ? Collections.emptyMap() : optionDefinition.getEffects());

        DialogueNodeDefinition nextNodeDefinition = resolveDialogueNode(definition, nextNodeId);
        DialogueNode nextNode = buildDialogueNode(nextNodeDefinition, definition, flags);
        result.nextNode(nextNode);

        return result;
    }

    @Override
    @SuppressWarnings("null")
    public QuestCompletionResult completeQuest(UUID instanceId, CompleteQuestRequest completeQuestRequest) {
        UUID questInstanceId = requireId(instanceId, "instance_id");
        QuestInstanceEntity instance = questInstanceRepository.findById(questInstanceId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quest instance not found"));

        if (instance.getStatus() == QuestStatus.COMPLETED) {
            return buildCompletionResult(instance, loadTemplateDefinition(requireText(instance.getQuestTemplateId(), "quest_template_id")));
        }

        instance.setStatus(QuestStatus.COMPLETED);
        instance.setCompletedAt(OffsetDateTime.now());

        Map<String, ProgressSnapshot> progress = readProgress(instance.getProgressJson());
        progress.values().forEach(snapshot -> {
            snapshot.setCurrent(snapshot.getTarget());
            snapshot.setCompleted(true);
        });
        instance.setProgressJson(writeProgress(progress));
        questInstanceRepository.save(instance);

        return buildCompletionResult(instance, loadTemplateDefinition(requireText(instance.getQuestTemplateId(), "quest_template_id")));
    }

    @Override
    @SuppressWarnings("null")
    @Transactional(readOnly = true)
    public GetActiveQuests200Response getActiveQuests(UUID characterId) {
        UUID safeCharacterId = requireId(characterId, "character_id");
        List<QuestInstanceEntity> instances = questInstanceRepository.findByCharacterIdAndStatus(safeCharacterId, QuestStatus.ACTIVE);
        List<QuestProgress> progressList = new ArrayList<>();

        for (QuestInstanceEntity instance : instances) {
            questRepository.findById(requireText(instance.getQuestTemplateId(), "quest_template_id")).ifPresent(template -> {
                Map<String, ProgressSnapshot> progress = readProgress(instance.getProgressJson());
                List<QuestObjectiveEntity> objectives = questObjectiveRepository.findByQuestIdOrderByOrderIndex(template.getId());
                progressList.add(buildQuestProgress(instance, template, progress, objectives));
            });
        }

        GetActiveQuests200Response response = new GetActiveQuests200Response();
        response.setQuests(progressList);
        return response;
    }

    @Override
    @SuppressWarnings("null")
    @Transactional(readOnly = true)
    public DialogueNode getCurrentDialogue(UUID instanceId) {
        UUID questInstanceId = requireId(instanceId, "instance_id");
        QuestInstanceEntity instance = questInstanceRepository.findById(questInstanceId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quest instance not found"));

        QuestTemplateDefinition definition = loadTemplateDefinition(requireText(instance.getQuestTemplateId(), "quest_template_id"));
        QuestDialogueStateEntity state = dialogueStateRepository.findByQuestInstanceId(questInstanceId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dialogue state not initialised"));

        DialogueNodeDefinition nodeDefinition = resolveDialogueNode(definition, state.getCurrentNodeId());
        Map<String, Object> flags = readFlags(instance.getFlagsJson());
        return buildDialogueNode(nodeDefinition, definition, flags);
    }

    @Override
    @SuppressWarnings("null")
    @Transactional(readOnly = true)
    public QuestInstance getQuestInstance(UUID instanceId) {
        UUID questInstanceId = requireId(instanceId, "instance_id");
        QuestInstanceEntity instance = questInstanceRepository.findById(questInstanceId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quest instance not found"));
        return toQuestInstance(instance);
    }

    @Override
    @SuppressWarnings("null")
    public SkillCheckResult performSkillCheck(UUID instanceId, SkillCheckRequest skillCheckRequest) {
        if (skillCheckRequest == null || !StringUtils.hasText(skillCheckRequest.getSkill())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Skill is required");
        }
        UUID questInstanceId = requireId(instanceId, "instance_id");
        QuestInstanceEntity instance = questInstanceRepository.findById(questInstanceId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quest instance not found"));

        SkillCheckRequirementDefinition requirement = new SkillCheckRequirementDefinition();
        requirement.setSkill(skillCheckRequest.getSkill());
        requirement.setDifficulty(skillCheckRequest.getDifficulty());
        requirement.setAdvantage(Boolean.TRUE.equals(skillCheckRequest.getAdvantage()));

        return executeSkillCheck(instance, requirement, false);
    }

    @Override
    @SuppressWarnings("null")
    public QuestInstance startQuest(StartQuestRequest startQuestRequest) {
        if (startQuestRequest == null || startQuestRequest.getCharacterId() == null || !StringUtils.hasText(startQuestRequest.getQuestTemplateId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "character_id and quest_template_id are required");
        }

        UUID characterId = requireId(startQuestRequest.getCharacterId(), "character_id");
        String questTemplateId = requireText(startQuestRequest.getQuestTemplateId(), "quest_template_id");

        if (questInstanceRepository.existsByCharacterIdAndQuestTemplateIdAndStatus(characterId, questTemplateId, QuestStatus.ACTIVE)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Quest is already active for this character");
        }

        QuestEntity template = questRepository.findById(questTemplateId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quest template not found"));

        QuestTemplateDefinition definition = loadTemplateDefinition(questTemplateId);
        String startNodeId = requireText(definition.getStartNodeId(), "start_node_id");

        List<QuestObjectiveEntity> objectives = questObjectiveRepository.findByQuestIdOrderByOrderIndex(template.getId());
        Map<String, ProgressSnapshot> progress = initialiseProgress(objectives);

        QuestInstanceEntity instance = QuestInstanceEntity.builder()
            .characterId(characterId)
            .questTemplateId(questTemplateId)
            .questName(template.getName())
            .status(QuestStatus.ACTIVE)
            .currentBranchId(definition.getStartBranchId())
            .currentDialogueNodeId(startNodeId)
            .progressJson(writeProgress(progress))
            .flagsJson(writeFlags(new HashMap<>()))
            .startedAt(OffsetDateTime.now())
            .build();
        QuestInstanceEntity savedInstance = Objects.requireNonNull(questInstanceRepository.save(instance));

        QuestDialogueStateEntity state = QuestDialogueStateEntity.builder()
            .questInstanceId(savedInstance.getId())
            .currentNodeId(startNodeId)
            .visitedNodesJson(writeVisitedNodes(new ArrayList<>()))
            .choicesMadeJson(writeChoices(new ArrayList<>()))
            .build();
        Objects.requireNonNull(dialogueStateRepository.save(state));

        return toQuestInstance(savedInstance);
    }

    private QuestInstance toQuestInstance(QuestInstanceEntity entity) {
        Map<String, ProgressSnapshot> progress = readProgress(entity.getProgressJson());
        Map<String, QuestInstanceProgressValue> dtoProgress = new LinkedHashMap<>();
        progress.forEach((key, snapshot) -> dtoProgress.put(key, new QuestInstanceProgressValue()
            .current(snapshot.getCurrent())
            .target(snapshot.getTarget())
            .completed(snapshot.isCompleted())));

        Map<String, Object> flags = readFlags(entity.getFlagsJson());

        QuestInstance dto = new QuestInstance()
            .id(entity.getId())
            .characterId(entity.getCharacterId())
            .questTemplateId(entity.getQuestTemplateId())
            .questName(entity.getQuestName())
            .status(QuestInstance.StatusEnum.fromValue(entity.getStatus().name()))
            .progress(dtoProgress)
            .flags(flags)
            .startedAt(entity.getStartedAt());

        if (StringUtils.hasText(entity.getCurrentBranchId())) {
            dto.currentBranchId(entity.getCurrentBranchId());
        }
        if (StringUtils.hasText(entity.getCurrentDialogueNodeId())) {
            dto.currentDialogueNodeId(entity.getCurrentDialogueNodeId());
        }
        if (entity.getCompletedAt() != null) {
            dto.completedAt(entity.getCompletedAt());
        }

        return dto;
    }

    @SuppressWarnings("null")
    private @NonNull QuestTemplateDefinition loadTemplateDefinition(@NonNull String questTemplateId) {
        return templateDefinitionRepository.findById(questTemplateId)
            .map(entity -> {
                try {
                    return objectMapper.readValue(entity.getDefinition(), QuestTemplateDefinition.class);
                } catch (JsonProcessingException ex) {
                    log.error("Failed to parse quest template definition {}: {}", questTemplateId, ex.getMessage());
                    throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Quest template definition invalid");
                }
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quest template definition not found"));
    }

    private Map<String, ProgressSnapshot> initialiseProgress(List<QuestObjectiveEntity> objectives) {
        Map<String, ProgressSnapshot> progress = new LinkedHashMap<>();
        for (QuestObjectiveEntity objective : objectives) {
            ProgressSnapshot snapshot = new ProgressSnapshot();
            snapshot.setCurrent(0);
            snapshot.setTarget(Optional.ofNullable(objective.getTargetProgress()).orElse(1));
            snapshot.setCompleted(false);
            progress.put(objective.getId(), snapshot);
        }
        return progress;
    }

    private Map<String, ProgressSnapshot> readProgress(@Nullable String json) {
        if (!StringUtils.hasText(json)) {
            return new LinkedHashMap<>();
        }
        try {
            return objectMapper.readValue(json, PROGRESS_TYPE);
        } catch (JsonProcessingException ex) {
            log.warn("Failed to parse quest progress JSON: {}", ex.getMessage());
            return new LinkedHashMap<>();
        }
    }

    private Map<String, Object> readFlags(@Nullable String json) {
        if (!StringUtils.hasText(json)) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(json, FLAGS_TYPE);
        } catch (JsonProcessingException ex) {
            log.warn("Failed to parse quest flags JSON: {}", ex.getMessage());
            return new HashMap<>();
        }
    }

    private String writeProgress(Map<String, ProgressSnapshot> progress) {
        try {
            return objectMapper.writeValueAsString(progress);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to serialise quest progress");
        }
    }

    private String writeFlags(Map<String, Object> flags) {
        try {
            return objectMapper.writeValueAsString(flags);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to serialise quest flags");
        }
    }

    private @NonNull DialogueNodeDefinition resolveDialogueNode(QuestTemplateDefinition definition, @NonNull String nodeId) {
        if (!StringUtils.hasText(nodeId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dialogue node id is empty");
        }
        DialogueNodeDefinition node = definition.getDialogueNodes().get(nodeId);
        if (node == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dialogue node not found");
        }
        node.setNodeId(nodeId);
        return node;
    }

    private DialogueNode buildDialogueNode(DialogueNodeDefinition nodeDefinition, QuestTemplateDefinition definition, Map<String, Object> flags) {
        DialogueNode node = new DialogueNode()
            .nodeId(nodeDefinition.getNodeId())
            .speaker(nodeDefinition.getSpeaker())
            .text(nodeDefinition.getText())
            .conditions(nodeDefinition.getConditions() == null ? Collections.emptyMap() : nodeDefinition.getConditions());

        List<DialogueOption> options = new ArrayList<>();
        for (DialogueOptionDefinition optionDefinition : nodeDefinition.getOptions()) {
            boolean available = isOptionAvailable(optionDefinition, flags);
            if (!available && Boolean.FALSE.equals(optionDefinition.getShowWhenLocked())) {
                continue;
            }
            DialogueOption option = new DialogueOption()
                .id(optionDefinition.getOptionId())
                .text(optionDefinition.getText())
                .available(available);
            optionDefinition.getSkillCheckOptional().ifPresent(option::requiresSkill);
            optionDefinition.getConsequenceOptional().ifPresent(option::consequence);
            options.add(option);
        }
        node.setOptions(options);
        return node;
    }

    private boolean isOptionAvailable(DialogueOptionDefinition optionDefinition, Map<String, Object> flags) {
        if (optionDefinition.getRequiredAttribute() != null) {
            int attributeValue = resolveAttributeValue(flags, optionDefinition.getRequiredAttribute().getAttribute());
            if (attributeValue < optionDefinition.getRequiredAttribute().getMinValue()) {
                return false;
            }
        }
        if (optionDefinition.getConditions() != null) {
            for (Map.Entry<String, Object> entry : optionDefinition.getConditions().entrySet()) {
                Object flagValue = resolveFlagValue(flags, entry.getKey());
                if (!Objects.equals(flagValue, entry.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean applyEffects(@Nullable Map<String, Object> effects,
                                 Map<String, ProgressSnapshot> progress,
                                 Map<String, Object> flags) {
        if (effects == null || effects.isEmpty()) {
            return false;
        }
        boolean updated = false;

        Object progressEffect = effects.get("progress");
        if (progressEffect instanceof Map<?, ?> progressMap) {
            for (Map.Entry<?, ?> entry : progressMap.entrySet()) {
                String key = String.valueOf(entry.getKey());
                ProgressSnapshot snapshot = progress.computeIfAbsent(key, unused -> new ProgressSnapshot());
                if (snapshot.getTarget() <= 0) {
                    snapshot.setTarget(1);
                }
                if (entry.getValue() instanceof Number number) {
                    int newValue = Math.min(snapshot.getTarget(), Math.max(0, snapshot.getCurrent() + number.intValue()));
                    snapshot.setCurrent(newValue);
                    snapshot.setCompleted(newValue >= snapshot.getTarget());
                    updated = true;
                } else if (entry.getValue() instanceof Map<?, ?> instruction) {
                    Object setValue = instruction.get("set_current");
                    if (setValue instanceof Number setNumber) {
                        snapshot.setCurrent(Math.min(snapshot.getTarget(), Math.max(0, setNumber.intValue())));
                        updated = true;
                    }
                    Object setTarget = instruction.get("set_target");
                    if (setTarget instanceof Number setTargetNumber) {
                        snapshot.setTarget(Math.max(1, setTargetNumber.intValue()));
                        updated = true;
                    }
                    Object setCompleted = instruction.get("set_completed");
                    if (setCompleted instanceof Boolean bool) {
                        snapshot.setCompleted(bool);
                        updated = true;
                    } else {
                        snapshot.setCompleted(snapshot.getCurrent() >= snapshot.getTarget());
                    }
                }
            }
        }

        Object flagsEffect = effects.get("flags");
        if (flagsEffect instanceof Map<?, ?> flagChanges) {
            for (Map.Entry<?, ?> entry : flagChanges.entrySet()) {
                flags.put(String.valueOf(entry.getKey()), entry.getValue());
                updated = true;
            }
        }

        return updated;
    }

    private void updateDialogueState(QuestDialogueStateEntity state, String nodeId, String optionId, String nextNodeId) {
        List<String> visitedNodes = readVisitedNodes(state.getVisitedNodesJson());
        if (!visitedNodes.contains(nodeId)) {
            visitedNodes.add(nodeId);
        }
        List<ChoiceRecord> choices = readChoices(state.getChoicesMadeJson());
        choices.add(new ChoiceRecord(nodeId, optionId, nextNodeId, OffsetDateTime.now()));

        state.setVisitedNodesJson(writeVisitedNodes(visitedNodes));
        state.setChoicesMadeJson(writeChoices(choices));
        state.setCurrentNodeId(nextNodeId);
        dialogueStateRepository.save(state);
    }

    private List<String> readVisitedNodes(@Nullable String json) {
        if (!StringUtils.hasText(json)) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, STRING_LIST_TYPE);
        } catch (JsonProcessingException ex) {
            log.warn("Failed to parse visited nodes JSON: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    private String writeVisitedNodes(List<String> visitedNodes) {
        try {
            return objectMapper.writeValueAsString(visitedNodes);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to serialise visited nodes");
        }
    }

    private List<ChoiceRecord> readChoices(@Nullable String json) {
        if (!StringUtils.hasText(json)) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, CHOICE_LIST_TYPE);
        } catch (JsonProcessingException ex) {
            log.warn("Failed to parse choice history JSON: {}", ex.getMessage());
            return new ArrayList<>();
        }
    }

    private String writeChoices(List<ChoiceRecord> choices) {
        try {
            return objectMapper.writeValueAsString(choices);
        } catch (JsonProcessingException ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to serialise choice history");
        }
    }

    private QuestProgress buildQuestProgress(QuestInstanceEntity instance,
                                             QuestEntity template,
                                             Map<String, ProgressSnapshot> progressMap,
                                             List<QuestObjectiveEntity> objectives) {
        QuestProgress progress = new QuestProgress()
            .id(template.getId())
            .name(template.getName())
            .description(template.getDescription())
            .type(mapQuestType(template.getType()))
            .level(Optional.ofNullable(template.getLevel()).orElse(1))
            .status(mapQuestStatus(instance.getStatus()))
            .startedAt(instance.getStartedAt());

        if (instance.getCompletedAt() != null) {
            progress.completedAt(instance.getCompletedAt());
        }

        progress.setRewards(buildQuestRewards(template));
        progress.setObjectives(buildObjectives(objectives, progressMap));
        progress.setIsRepeatable(false);

        return progress;
    }

    private QuestProgress.TypeEnum mapQuestType(@Nullable QuestEntity.QuestType questType) {
        if (questType == null) {
            return QuestProgress.TypeEnum.SIDE;
        }
        return switch (questType) {
            case MAIN -> QuestProgress.TypeEnum.MAIN;
            case SIDE -> QuestProgress.TypeEnum.SIDE;
            case CONTRACT -> QuestProgress.TypeEnum.CONTRACT;
        };
    }

    private QuestProgress.StatusEnum mapQuestStatus(QuestStatus status) {
        return switch (status) {
            case ACTIVE -> QuestProgress.StatusEnum.ACTIVE;
            case COMPLETED -> QuestProgress.StatusEnum.COMPLETED;
            case FAILED -> QuestProgress.StatusEnum.FAILED;
            case ABANDONED -> QuestProgress.StatusEnum.ABANDONED;
        };
    }

    private QuestRewards buildQuestRewards(QuestEntity template) {
        QuestRewards rewards = new QuestRewards()
            .experience(Optional.ofNullable(template.getRewardExperience()).orElse(0))
            .currency(Optional.ofNullable(template.getRewardMoney()).orElse(0));

        if (StringUtils.hasText(template.getRewardReputationFaction()) && template.getRewardReputationAmount() != null) {
            rewards.putReputationItem(template.getRewardReputationFaction(), template.getRewardReputationAmount());
        }

        if (StringUtils.hasText(template.getRewardItems())) {
            try {
                List<Map<String, Object>> items = objectMapper.readValue(template.getRewardItems(), new TypeReference<>() {});
                for (Map<String, Object> item : items) {
                    QuestRewardsItemsInner dto = new QuestRewardsItemsInner();
                    dto.setItemId(Objects.toString(item.get("item_id"), null));
                    if (item.get("quantity") instanceof Number number) {
                        dto.setQuantity(number.intValue());
                    }
                    rewards.addItemsItem(dto);
                }
            } catch (JsonProcessingException ex) {
                log.warn("Failed to parse quest reward items for {}: {}", template.getId(), ex.getMessage());
            }
        }

        return rewards;
    }

    private List<QuestObjective> buildObjectives(List<QuestObjectiveEntity> objectives,
                                                 Map<String, ProgressSnapshot> progressMap) {
        List<QuestObjective> result = new ArrayList<>();
        for (QuestObjectiveEntity entity : objectives) {
            ProgressSnapshot snapshot = progressMap.getOrDefault(entity.getId(), new ProgressSnapshot());
            QuestObjective dto = new QuestObjective()
                .id(entity.getId())
                .description(entity.getDescription())
                .type(mapObjectiveType(entity.getType()))
                .currentProgress(snapshot.getCurrent())
                .targetProgress(snapshot.getTarget() > 0 ? snapshot.getTarget() : Optional.ofNullable(entity.getTargetProgress()).orElse(1))
                .completed(snapshot.isCompleted());
            dto.setOptional(Boolean.TRUE.equals(entity.getOptional()));
            result.add(dto);
        }
        return result;
    }

    private QuestObjective.TypeEnum mapObjectiveType(ObjectiveType type) {
        if (type == null) {
            return QuestObjective.TypeEnum.INTERACT;
        }
        return switch (type) {
            case LOCATION -> QuestObjective.TypeEnum.LOCATION;
            case KILL -> QuestObjective.TypeEnum.KILL;
            case COLLECT -> QuestObjective.TypeEnum.COLLECT;
            case TALK -> QuestObjective.TypeEnum.TALK;
            case INTERACT -> QuestObjective.TypeEnum.INTERACT;
        };
    }

    @SuppressWarnings("null")
    private SkillCheckResult executeSkillCheck(QuestInstanceEntity instance,
                                               SkillCheckRequirementDefinition requirement,
                                               boolean persistResult) {
        int primaryRoll = rollD20();
        Integer secondaryRoll = null;
        int modifier = resolveSkillModifier(instance, requirement.getSkill());
        boolean advantageUsed = requirement.isAdvantage();

        if (advantageUsed) {
            secondaryRoll = rollD20();
            primaryRoll = Math.max(primaryRoll, secondaryRoll);
        }

        int total = primaryRoll + modifier;
        boolean success = total >= requirement.getDifficulty();

        SkillCheckResult result = new SkillCheckResult()
            .skill(requirement.getSkill())
            .difficulty(requirement.getDifficulty())
            .roll(primaryRoll)
            .modifier(modifier)
            .total(total)
            .success(success)
            .criticalSuccess(primaryRoll == 20)
            .criticalFailure(primaryRoll == 1)
            .advantageUsed(advantageUsed)
            .rolls(secondaryRoll == null ? Collections.singletonList(primaryRoll) : List.of(primaryRoll, secondaryRoll));

        if (persistResult) {
            QuestSkillCheckResultEntity entity = QuestSkillCheckResultEntity.builder()
                .questInstanceId(instance.getId())
                .dialogueNodeId(instance.getCurrentDialogueNodeId())
                .skillName(requirement.getSkill())
                .difficultyClass(requirement.getDifficulty())
                .diceRoll(result.getRoll())
                .secondaryRoll(secondaryRoll)
                .skillModifier(modifier)
                .totalResult(total)
                .success(success)
                .criticalSuccess(Boolean.TRUE.equals(result.getCriticalSuccess()))
                .criticalFailure(Boolean.TRUE.equals(result.getCriticalFailure()))
                .advantageUsed(advantageUsed)
                .build();
            Objects.requireNonNull(skillCheckResultRepository.save(entity));
        }

        return result;
    }

    private int rollD20() {
        return ThreadLocalRandom.current().nextInt(1, 21);
    }

    private int resolveSkillModifier(QuestInstanceEntity instance, String skill) {
        Map<String, Object> flags = readFlags(instance.getFlagsJson());
        Object modifiers = flags.get("skill_modifiers");
        if (modifiers instanceof Map<?, ?> map) {
            Object value = map.get(skill);
            if (value instanceof Number number) {
                return number.intValue();
            }
        }
        return 0;
    }

    private int resolveAttributeValue(Map<String, Object> flags, String attribute) {
        if (!StringUtils.hasText(attribute)) {
            return Integer.MAX_VALUE;
        }
        Object attributes = flags.get("attributes");
        if (attributes instanceof Map<?, ?> map) {
            Object value = map.get(attribute);
            if (value instanceof Number number) {
                return number.intValue();
            }
        }
        return Integer.MAX_VALUE;
    }

    private Object resolveFlagValue(Map<String, Object> flags, String key) {
        if (!StringUtils.hasText(key)) {
            return null;
        }
        if (!key.contains(".")) {
            return flags.get(key);
        }
        String[] parts = key.split("\\.");
        Object current = flags;
        for (String part : parts) {
            if (!(current instanceof Map<?, ?> map)) {
                return null;
            }
            current = map.get(part);
        }
        return current;
    }

    @NonNull
    private Pageable createPageable(@Nullable Integer page, @Nullable Integer pageSize) {
        int pageIndex = page == null || page < 1 ? 0 : page - 1;
        int size = pageSize == null || pageSize < 1 ? 20 : pageSize;
        return PageRequest.of(pageIndex, size);
    }

    @NonNull
    private Specification<QuestCatalogEntryEntity> buildQuestCatalogSpecification(
        String type,
        String period,
        String difficulty,
        String faction,
        Integer minLevel,
        Integer maxLevel,
        Boolean hasRomance,
        Boolean hasCombat,
        Integer estimatedTimeMin,
        Integer estimatedTimeMax
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(type)) {
                predicates.add(cb.equal(root.get("type"), type.trim().toUpperCase(Locale.ROOT)));
            }

            if (StringUtils.hasText(period)) {
                predicates.add(cb.equal(root.get("period"), period.trim()));
            }

            if (StringUtils.hasText(difficulty)) {
                predicates.add(cb.equal(root.get("difficulty"), difficulty.trim().toUpperCase(Locale.ROOT)));
            }

            if (StringUtils.hasText(faction)) {
                String factionLower = faction.trim().toLowerCase(Locale.ROOT);
                predicates.add(cb.equal(cb.lower(root.get("faction")), factionLower));
            }

            if (minLevel != null) {
                predicates.add(cb.greaterThanOrEqualTo(cb.coalesce(root.get("levelRequirement"), cb.literal(0)), minLevel));
            }

            if (maxLevel != null) {
                predicates.add(cb.lessThanOrEqualTo(cb.coalesce(root.get("levelRequirement"), cb.literal(Integer.MAX_VALUE)), maxLevel));
            }

            if (hasRomance != null) {
                predicates.add(cb.equal(root.get("hasRomance"), hasRomance));
            }

            if (hasCombat != null) {
                predicates.add(cb.equal(root.get("hasCombat"), hasCombat));
            }

            if (estimatedTimeMin != null) {
                predicates.add(cb.greaterThanOrEqualTo(cb.coalesce(root.get("estimatedTimeMinutes"), cb.literal(0)), estimatedTimeMin));
            }

            if (estimatedTimeMax != null) {
                predicates.add(cb.lessThanOrEqualTo(cb.coalesce(root.get("estimatedTimeMinutes"), cb.literal(Integer.MAX_VALUE)), estimatedTimeMax));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    @NonNull
    private Specification<QuestCatalogEntryEntity> buildQuestCatalogSearchSpecification(String queryText, List<String> searchIn) {
        return (root, cq, cb) -> {
            String pattern = "%" + queryText.toLowerCase(Locale.ROOT) + "%";
            List<String> fields = resolveSearchFields(searchIn);
            List<Predicate> likePredicates = new ArrayList<>();
            for (String field : fields) {
                likePredicates.add(cb.like(cb.lower(root.get(field)), pattern));
            }
            if (likePredicates.isEmpty()) {
                likePredicates.add(cb.like(cb.lower(root.get("title")), pattern));
            }
            return cb.or(likePredicates.toArray(new Predicate[0]));
        };
    }

    private List<String> resolveSearchFields(List<String> searchIn) {
        if (searchIn == null || searchIn.isEmpty()) {
            return List.of("title", "description", "fullDescription", "storyline");
        }
        List<String> fields = new ArrayList<>();
        for (String item : searchIn) {
            if (!StringUtils.hasText(item)) {
                continue;
            }
            switch (item.trim().toUpperCase(Locale.ROOT)) {
                case "TITLE" -> fields.add("title");
                case "DESCRIPTION" -> fields.add("description");
                case "NPC_NAMES" -> fields.add("keyNpcsJson");
                case "LOCATIONS" -> fields.add("locationsJson");
                case "TAGS" -> fields.add("tagsJson");
                case "STORYLINE" -> fields.add("storyline");
                default -> {
                }
            }
        }
        if (fields.isEmpty()) {
            fields.addAll(List.of("title", "description", "fullDescription", "storyline"));
        }
        return fields;
    }

    private QuestCatalogEntryEntity findQuestCatalogEntry(@NonNull String questId) {
        return questCatalogEntryRepository.findById(questId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "quest not found"));
    }

    private QuestCatalogEntry toQuestCatalogEntry(QuestCatalogEntryEntity entity) {
        QuestCatalogEntry entry = new QuestCatalogEntry()
            .questId(entity.getQuestId())
            .title(entity.getTitle())
            .description(entity.getDescription())
            .period(entity.getPeriod())
            .levelRequirement(entity.getLevelRequirement())
            .estimatedTimeMinutes(entity.getEstimatedTimeMinutes());

        if (StringUtils.hasText(entity.getType())) {
            try {
                entry.type(QuestCatalogEntry.TypeEnum.fromValue(entity.getType()));
            } catch (IllegalArgumentException ex) {
                entry.type(QuestCatalogEntry.TypeEnum.SIDE);
            }
        } else {
            entry.type(QuestCatalogEntry.TypeEnum.SIDE);
        }

        if (StringUtils.hasText(entity.getDifficulty())) {
            try {
                entry.difficulty(QuestCatalogEntry.DifficultyEnum.fromValue(entity.getDifficulty()));
            } catch (IllegalArgumentException ex) {
                entry.difficulty(QuestCatalogEntry.DifficultyEnum.MEDIUM);
            }
        } else {
            entry.difficulty(QuestCatalogEntry.DifficultyEnum.MEDIUM);
        }

        if (StringUtils.hasText(entity.getFaction())) {
            entry.faction(entity.getFaction());
        }

        List<String> tags = readValue(entity.getTagsJson(), STRING_LIST_TYPE);
        entry.tags(tags == null ? new ArrayList<>() : tags);

        QuestCatalogEntryRewardsSummary summary = toRewardsSummary(entity.getRewardsSummaryJson());
        if (summary != null) {
            entry.rewardsSummary(summary);
        }

        if (entity.getCompletionRate() != null) {
            entry.completionRate(safeBigDecimalToFloat(entity.getCompletionRate()));
        }

        if (entity.getAverageRating() != null) {
            entry.averageRating(safeBigDecimalToFloat(entity.getAverageRating()));
        }

        return entry;
    }

    private QuestDetails toQuestDetails(QuestCatalogEntryEntity entity) {
        QuestCatalogEntry base = toQuestCatalogEntry(entity);

        QuestDetails details = new QuestDetails()
            .questId(base.getQuestId())
            .title(base.getTitle())
            .description(base.getDescription())
            .period(base.getPeriod())
            .levelRequirement(base.getLevelRequirement())
            .estimatedTimeMinutes(base.getEstimatedTimeMinutes())
            .branchesCount(Optional.ofNullable(entity.getBranchesCount()).orElse(0))
            .endingsCount(Optional.ofNullable(entity.getEndingsCount()).orElse(0))
            .hasDialogueTree(Boolean.TRUE.equals(entity.getHasDialogueTree()))
            .hasSkillChecks(Boolean.TRUE.equals(entity.getHasSkillChecks()))
            .hasCombat(Boolean.TRUE.equals(entity.getHasCombat()))
            .hasRomance(Boolean.TRUE.equals(entity.getHasRomance()))
            .fullDescription(entity.getFullDescription())
            .storyline(entity.getStoryline());

        QuestCatalogEntry.TypeEnum baseType = base.getType();
        if (baseType != null) {
            details.type(QuestDetails.TypeEnum.fromValue(baseType.getValue()));
        }

        QuestCatalogEntry.DifficultyEnum baseDifficulty = base.getDifficulty();
        if (baseDifficulty != null) {
            details.difficulty(QuestDetails.DifficultyEnum.fromValue(baseDifficulty.getValue()));
        }

        if (base.getFaction() != null && base.getFaction().isPresent()) {
            details.faction(base.getFaction().get());
        }

        if (base.getTags() != null) {
            details.tags(new ArrayList<>(base.getTags()));
        }

        QuestCatalogEntryRewardsSummary baseSummary = base.getRewardsSummary();
        if (baseSummary != null) {
            details.rewardsSummary(new QuestCatalogEntryRewardsSummary()
                .experience(baseSummary.getExperience())
                .eddies(baseSummary.getEddies())
                .itemsCount(baseSummary.getItemsCount()));
        }

        details.completionRate(base.getCompletionRate());
        details.averageRating(base.getAverageRating());

        List<QuestDetailsAllOfObjectives> objectives = readValue(entity.getObjectivesJson(), QUEST_DETAILS_OBJECTIVES_TYPE);
        if (objectives != null) {
            details.objectives(objectives);
        }

        List<QuestDetailsAllOfKeyNpcs> keyNpcs = readValue(entity.getKeyNpcsJson(), QUEST_DETAILS_KEY_NPCS_TYPE);
        if (keyNpcs != null) {
            details.keyNpcs(keyNpcs);
        }

        List<String> locations = readValue(entity.getLocationsJson(), STRING_LIST_TYPE);
        if (locations != null) {
            details.locations(locations);
        }

        List<String> prerequisites = readValue(entity.getPrerequisitesJson(), STRING_LIST_TYPE);
        if (prerequisites != null) {
            details.prerequisites(prerequisites);
        }

        List<String> unlocks = readValue(entity.getUnlocksJson(), STRING_LIST_TYPE);
        if (unlocks != null) {
            details.unlocks(unlocks);
        }

        QuestDetailsAllOfRewardsDetailed rewardsDetailed = readValue(entity.getRewardsDetailedJson(), QUEST_DETAILS_REWARDS_TYPE);
        if (rewardsDetailed != null) {
            details.rewardsDetailed(rewardsDetailed);
        }

        return details;
    }

    private QuestLootTable toQuestLootTable(QuestCatalogEntryEntity entity) {
        QuestLootTable table = new QuestLootTable()
            .questId(entity.getQuestId());

        Map<String, Object> json = readValue(entity.getLootTableJson(), STRING_OBJECT_MAP_TYPE);
        if (json == null || json.isEmpty()) {
            return table;
        }

        List<Map<String, Object>> guaranteed = castToListOfMap(json.get("guaranteed_loot"));
        if (guaranteed != null) {
            List<QuestLootTableGuaranteedLootInner> guaranteedLoot = guaranteed.stream()
                .map(item -> new QuestLootTableGuaranteedLootInner()
                    .itemId(parseUuid(item.get("item_id")))
                    .quantity(((Number) item.getOrDefault("quantity", 1)).intValue()))
                .collect(Collectors.toList());
            table.guaranteedLoot(guaranteedLoot);
        }

        List<Map<String, Object>> random = castToListOfMap(json.get("random_loot"));
        if (random != null) {
            List<QuestLootTableRandomLootInner> randomLoot = random.stream()
                .map(item -> {
                    Map<String, Object> quantityMap = toMap(item.get("quantity_range"));
                    QuestLootTableRandomLootInnerQuantityRange range = new QuestLootTableRandomLootInnerQuantityRange()
                        .min(((Number) quantityMap.getOrDefault("min", 1)).intValue())
                        .max(((Number) quantityMap.getOrDefault("max", quantityMap.getOrDefault("min", 1))).intValue());
                    return new QuestLootTableRandomLootInner()
                        .itemId(parseUuid(item.get("item_id")))
                        .dropChance(((Number) item.getOrDefault("drop_chance", 0)).floatValue())
                        .quantityRange(range);
                })
                .collect(Collectors.toList());
            table.randomLoot(randomLoot);
        }

        return table;
    }

    private QuestSearchResult toQuestSearchResult(QuestCatalogEntryEntity entity, String query) {
        QuestCatalogEntry base = toQuestCatalogEntry(entity);
        QuestSearchResult result = new QuestSearchResult()
            .questId(base.getQuestId())
            .title(base.getTitle())
            .description(base.getDescription())
            .period(base.getPeriod())
            .levelRequirement(base.getLevelRequirement())
            .estimatedTimeMinutes(base.getEstimatedTimeMinutes())
            .completionRate(base.getCompletionRate())
            .averageRating(base.getAverageRating())
            .highlightedText(extractHighlight(entity, query));

        QuestCatalogEntry.TypeEnum searchType = base.getType();
        if (searchType != null) {
            result.type(QuestSearchResult.TypeEnum.fromValue(searchType.getValue()));
        }
        QuestCatalogEntry.DifficultyEnum searchDifficulty = base.getDifficulty();
        if (searchDifficulty != null) {
            result.difficulty(QuestSearchResult.DifficultyEnum.fromValue(searchDifficulty.getValue()));
        }
        if (base.getFaction() != null && base.getFaction().isPresent()) {
            result.faction(base.getFaction().get());
        }
        if (base.getTags() != null) {
            result.tags(new ArrayList<>(base.getTags()));
        }
        QuestCatalogEntryRewardsSummary searchSummary = base.getRewardsSummary();
        if (searchSummary != null) {
            result.rewardsSummary(new QuestCatalogEntryRewardsSummary()
                .experience(searchSummary.getExperience())
                .eddies(searchSummary.getEddies())
                .itemsCount(searchSummary.getItemsCount()));
        }

        String lowerQuery = query.toLowerCase(Locale.ROOT);
        float score = 60f;
        if (StringUtils.hasText(entity.getTitle()) && entity.getTitle().toLowerCase(Locale.ROOT).contains(lowerQuery)) {
            score += 25f;
        } else if (StringUtils.hasText(entity.getDescription()) && entity.getDescription().toLowerCase(Locale.ROOT).contains(lowerQuery)) {
            score += 15f;
        } else if (StringUtils.hasText(entity.getFullDescription()) && entity.getFullDescription().toLowerCase(Locale.ROOT).contains(lowerQuery)) {
            score += 10f;
        } else {
            score += 5f;
        }
        if (Boolean.TRUE.equals(entity.getHasCombat())) {
            score += 5f;
        }
        if (Boolean.TRUE.equals(entity.getHasRomance())) {
            score += 3f;
        }
        score = Math.min(100f, score);
        result.matchScore(score);
        return result;
    }

    private String extractHighlight(QuestCatalogEntryEntity entity, String query) {
        String lowerQuery = query.toLowerCase(Locale.ROOT);
        List<String> sources = new ArrayList<>();
        if (StringUtils.hasText(entity.getDescription())) {
            sources.add(entity.getDescription());
        }
        if (StringUtils.hasText(entity.getFullDescription())) {
            sources.add(entity.getFullDescription());
        }
        if (StringUtils.hasText(entity.getStoryline())) {
            sources.add(entity.getStoryline());
        }
        for (String source : sources) {
            int idx = source.toLowerCase(Locale.ROOT).indexOf(lowerQuery);
            if (idx >= 0) {
                int start = Math.max(0, idx - 40);
                int end = Math.min(source.length(), idx + query.length() + 40);
                return source.substring(start, end) + (end < source.length() ? "…" : "");
            }
        }
        return sources.stream().findFirst().map(text -> text.length() > 80 ? text.substring(0, 77) + "…" : text).orElse("Совпадений не найдено");
    }

    private GetQuestRecommendations200ResponseRecommendationsInner buildRecommendation(QuestCatalogEntryEntity entity, int characterLevel) {
        QuestCatalogEntry quest = toQuestCatalogEntry(entity);
        List<String> reasons = new ArrayList<>();

        if (entity.getLevelRequirement() != null) {
            int diff = Math.abs(entity.getLevelRequirement() - characterLevel);
            reasons.add(diff <= 2 ? "Уровень квеста близок к уровню персонажа" : "Квест рассчитан на другой уровень, но может быть интересен");
        } else {
            reasons.add("Квест доступен для любого уровня");
        }

        if (Boolean.TRUE.equals(entity.getHasCombat())) {
            reasons.add("Содержит боевые столкновения");
        }
        if (Boolean.TRUE.equals(entity.getHasRomance())) {
            reasons.add("Есть романтическая линия");
        }
        if (Boolean.TRUE.equals(entity.getHasDialogueTree())) {
            reasons.add("Включает разветвленные диалоги");
        }

        float score = 60f;
        if (entity.getLevelRequirement() != null) {
            int diff = Math.abs(entity.getLevelRequirement() - characterLevel);
            score += Math.max(0, 20 - diff * 3);
        }
        score += Boolean.TRUE.equals(entity.getHasCombat()) ? 5f : 0f;
        score += Boolean.TRUE.equals(entity.getHasRomance()) ? 5f : 0f;
        score += Boolean.TRUE.equals(entity.getHasDialogueTree()) ? 5f : 0f;
        if (entity.getAverageRating() != null) {
            score += Math.min(10f, safeBigDecimalToFloat(entity.getAverageRating()) * 2f);
        }
        score = Math.max(10f, Math.min(score, 100f));

        return new GetQuestRecommendations200ResponseRecommendationsInner()
            .quest(quest)
            .matchScore(score)
            .reasons(reasons);
    }

    private QuestChain toQuestChain(String storyline, List<QuestCatalogEntryEntity> quests) {
        List<QuestCatalogEntryEntity> sorted = quests.stream()
            .sorted(Comparator.comparing(entity -> Optional.ofNullable(entity.getLevelRequirement()).orElse(0)))
            .collect(Collectors.toList());

        List<QuestChainQuestsInner> questItems = new ArrayList<>();
        int index = 0;
        for (QuestCatalogEntryEntity entity : sorted) {
            questItems.add(new QuestChainQuestsInner()
                .questId(entity.getQuestId())
                .title(entity.getTitle())
                .order(index++)
                .optional(Boolean.FALSE));
        }

        Map<String, Object> rewards = aggregateRewards(sorted);

        return new QuestChain()
            .chainId(slugify(storyline))
            .name(storyline)
            .description(sorted.stream().findFirst().map(QuestCatalogEntryEntity::getDescription).orElse(null))
            .quests(questItems)
            .totalRewards(rewards.isEmpty() ? null : rewards);
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> castToListOfMap(Object value) {
        if (value instanceof List<?> list) {
            return list.stream()
                .map(item -> {
                    if (item instanceof Map<?, ?> map) {
                        return (Map<String, Object>) map;
                    }
                    if (item instanceof String str) {
                        Map<String, Object> result = new LinkedHashMap<>();
                        result.put("item_id", str);
                        result.put("quantity", 1);
                        return result;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        }
        return null;
    }

    private QuestCatalogEntryRewardsSummary toRewardsSummary(String json) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        Map<String, Integer> map = readValue(json, REWARDS_SUMMARY_MAP_TYPE);
        if (map == null) {
            return null;
        }
        return new QuestCatalogEntryRewardsSummary()
            .experience(map.getOrDefault("experience", 0))
            .eddies(map.getOrDefault("eddies", 0))
            .itemsCount(map.getOrDefault("items_count", 0));
    }

    private float safeBigDecimalToFloat(BigDecimal value) {
        return value == null ? 0f : value.setScale(2, RoundingMode.HALF_UP).floatValue();
    }

    private Map<String, Object> aggregateRewards(List<QuestCatalogEntryEntity> entries) {
        int totalExperience = 0;
        int totalEddies = 0;
        int totalItems = 0;
        for (QuestCatalogEntryEntity entity : entries) {
            QuestCatalogEntryRewardsSummary summary = toRewardsSummary(entity.getRewardsSummaryJson());
            if (summary != null) {
                totalExperience += Optional.ofNullable(summary.getExperience()).orElse(0);
                totalEddies += Optional.ofNullable(summary.getEddies()).orElse(0);
                totalItems += Optional.ofNullable(summary.getItemsCount()).orElse(0);
            }
        }
        Map<String, Object> rewards = new LinkedHashMap<>();
        if (totalExperience > 0) {
            rewards.put("experience", totalExperience);
        }
        if (totalEddies > 0) {
            rewards.put("eddies", totalEddies);
        }
        if (totalItems > 0) {
            rewards.put("items_count", totalItems);
        }
        return rewards;
    }

    private String slugify(String value) {
        if (!StringUtils.hasText(value)) {
            return UUID.randomUUID().toString();
        }
        return value.trim().toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
    }

    private UUID parseUuid(Object value) {
        if (value == null) {
            return null;
        }
        String text = value.toString();
        try {
            return UUID.fromString(text);
        } catch (IllegalArgumentException ex) {
            return UUID.nameUUIDFromBytes(text.getBytes(StandardCharsets.UTF_8));
        }
    }

    private String normalizeFilter(@Nullable String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return StringUtils.hasText(trimmed) ? trimmed.toLowerCase(Locale.ROOT) : null;
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> toMap(Object value) {
        if (value instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        return Collections.emptyMap();
    }

    @NonNull
    private Specification<FactionQuestEntity> buildFactionQuestSpecification(String faction, Integer minReputation, Integer playerLevelMin) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(faction)) {
                FactionQuestEntity.Faction factionEnum;
                try {
                    factionEnum = FactionQuestEntity.Faction.fromCode(faction);
                } catch (IllegalArgumentException ex) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "unknown faction value");
                }
                predicates.add(cb.equal(root.get("faction"), factionEnum));
            }

            if (minReputation != null) {
                predicates.add(cb.or(
                    cb.isNull(root.get("minReputationRequired")),
                    cb.lessThanOrEqualTo(root.get("minReputationRequired"), minReputation)
                ));
            }

            if (playerLevelMin != null) {
                predicates.add(cb.or(
                    cb.isNull(root.get("minLevelRequirement")),
                    cb.lessThanOrEqualTo(root.get("minLevelRequirement"), playerLevelMin)
                ));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private FactionQuest toFactionQuest(FactionQuestEntity entity) {
        FactionQuest quest = new FactionQuest()
            .questId(entity.getQuestId())
            .title(entity.getTitle())
            .description(entity.getDescription())
            .category(entity.getCategory())
            .branchesCount(entity.getBranchesCount())
            .endingsCount(entity.getEndingsCount())
            .estimatedTimeMinutes(entity.getEstimatedTimeMinutes())
            .requirements(readValue(entity.getRequirementsJson(), QuestRequirements.class))
            .rewards(readValue(entity.getRewardsJson(), QuestRewards.class));

        if (entity.getFaction() != null) {
            quest.setFaction(FactionEnum.fromValue(entity.getFaction().name()));
        }
        if (entity.getDifficulty() != null) {
            quest.setDifficulty(DifficultyEnum.fromValue(entity.getDifficulty().name()));
        }
        return quest;
    }

    private FactionQuestDetailed toFactionQuestDetailed(FactionQuestEntity entity,
                                                        List<FactionQuestBranchEntity> branches) {
        FactionQuest base = toFactionQuest(entity);
        FactionQuestDetailed detailed = new FactionQuestDetailed()
            .questId(base.getQuestId())
            .title(base.getTitle())
            .description(base.getDescription())
            .category(base.getCategory())
            .branchesCount(base.getBranchesCount())
            .endingsCount(base.getEndingsCount())
            .estimatedTimeMinutes(base.getEstimatedTimeMinutes())
            .requirements(base.getRequirements())
            .rewards(base.getRewards())
            .storyline(entity.getStoryline())
            .branches(branches.stream().map(this::toQuestBranch).collect(Collectors.toList()));

        FactionQuest.FactionEnum faction = base.getFaction();
        if (faction != null) {
            detailed.setFaction(FactionQuestDetailed.FactionEnum.fromValue(faction.getValue()));
        }
        FactionQuest.DifficultyEnum difficulty = base.getDifficulty();
        if (difficulty != null) {
            detailed.setDifficulty(FactionQuestDetailed.DifficultyEnum.fromValue(difficulty.getValue()));
        }

        detailed.setKeyNpcs(new ArrayList<>(readList(entity.getKeyNpcsJson(), KEY_NPC_LIST_TYPE)));
        detailed.setLocations(new ArrayList<>(readList(entity.getLocationsJson(), STRING_LIST_TYPE)));

        return detailed;
    }

    private QuestBranch toQuestBranch(FactionQuestBranchEntity entity) {
        QuestBranch branch = readValue(entity.getBranchPayload(), QuestBranch.class);
        if (branch == null) {
            branch = new QuestBranch();
        }
        branch.setBranchId(entity.getBranchId());
        return branch;
    }

    private QuestEnding toQuestEnding(FactionQuestEndingEntity entity) {
        QuestEnding ending = readValue(entity.getEndingPayload(), QuestEnding.class);
        if (ending == null) {
            ending = new QuestEnding();
        }
        ending.setEndingId(entity.getEndingId());
        return ending;
    }

    private ActiveQuestProgress toActiveQuestProgress(CharacterFactionQuestProgressEntity entry) {
        ActiveQuestProgress progress = new ActiveQuestProgress()
            .questId(entry.getQuestId())
            .currentBranch(entry.getCurrentBranch());

        progress.setChoicesMade(new ArrayList<>(readList(entry.getChoicesJson(), PROGRESS_CHOICES_LIST_TYPE)));
        progress.setObjectives(new ArrayList<>(readList(entry.getObjectivesJson(), PROGRESS_OBJECTIVES_LIST_TYPE)));
        return progress;
    }

    private GetFactionQuestProgress200ResponseCompletedQuestsInner toCompletedQuestProgress(CharacterFactionQuestProgressEntity entry) {
        return new GetFactionQuestProgress200ResponseCompletedQuestsInner()
            .questId(entry.getQuestId())
            .endingAchieved(entry.getEndingAchieved())
            .completionDate(entry.getCompletionDate());
    }

    private int safeLongToInt(long value) {
        return value > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) value;
    }

    private <T> T readValue(@Nullable String json, TypeReference<T> typeReference) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException ex) {
            log.warn("Failed to read JSON value for type {}: {}", typeReference.getType().getTypeName(), ex.getMessage());
            return null;
        }
    }

    private <T> T readValue(@Nullable String json, Class<T> type) {
        if (!StringUtils.hasText(json)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException ex) {
            log.warn("Failed to parse {}: {}", type.getSimpleName(), ex.getMessage());
            return null;
        }
    }

    private <T> List<T> readList(String json, TypeReference<List<T>> typeReference) {
        if (!StringUtils.hasText(json)) {
            return Collections.emptyList();
        }
        try {
            List<T> values = objectMapper.readValue(json, typeReference);
            return values == null ? Collections.emptyList() : values;
        } catch (JsonProcessingException ex) {
            log.warn("Failed to parse list payload: {}", ex.getMessage());
            return Collections.emptyList();
        }
    }

    private QuestCompletionResult buildCompletionResult(QuestInstanceEntity instance, QuestTemplateDefinition definition) {
        QuestCompletionResult result = new QuestCompletionResult()
            .questId(instance.getId())
            .completionTime(instance.getCompletedAt() != null ? instance.getCompletedAt() : OffsetDateTime.now());

        QuestRewards rewards = definition.getRewards() != null
            ? definition.getRewards().toDto()
            : questRepository.findById(instance.getQuestTemplateId())
                .map(this::buildQuestRewards)
                .orElse(null);
        result.setRewards(rewards);

        if (definition.getUnlockedQuests() != null) {
            result.setUnlockedQuests(definition.getUnlockedQuests());
        }
        if (definition.getReputationChanges() != null) {
            result.setReputationChanges(definition.getReputationChanges());
        }
        return result;
    }

    private @NonNull UUID requireId(UUID value, String fieldName) {
        if (value == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " is required");
        }
        return value;
    }

    private @NonNull String requireText(@Nullable String value, String fieldName) {
        if (!StringUtils.hasText(value)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, fieldName + " is required");
        }
        return Objects.requireNonNull(value);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class QuestTemplateDefinition {
        @JsonProperty("start_branch_id")
        private String startBranchId;
        @JsonProperty("start_node_id")
        private String startNodeId;
        @JsonProperty("dialogue_nodes")
        private Map<String, DialogueNodeDefinition> dialogueNodes = new LinkedHashMap<>();
        @JsonProperty("rewards")
        private QuestRewardsDefinition rewards;
        @JsonProperty("unlocked_quests")
        private List<String> unlockedQuests = new ArrayList<>();
        @JsonProperty("reputation_changes")
        private Map<String, Integer> reputationChanges = new HashMap<>();

        public String getStartBranchId() {
            return startBranchId;
        }

        public String getStartNodeId() {
            return startNodeId;
        }

        public Map<String, DialogueNodeDefinition> getDialogueNodes() {
            return dialogueNodes;
        }

        public QuestRewardsDefinition getRewards() {
            return rewards;
        }

        public List<String> getUnlockedQuests() {
            return unlockedQuests;
        }

        public Map<String, Integer> getReputationChanges() {
            return reputationChanges;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class DialogueNodeDefinition {
        @JsonProperty("node_id")
        private String nodeId;
        @JsonProperty("speaker")
        private String speaker;
        @JsonProperty("text")
        private String text;
        @JsonProperty("options")
        private List<DialogueOptionDefinition> options = new ArrayList<>();
        @JsonProperty("conditions")
        private Map<String, Object> conditions = new HashMap<>();

        public String getNodeId() {
            return nodeId;
        }

        public void setNodeId(String nodeId) {
            this.nodeId = nodeId;
        }

        public String getSpeaker() {
            return speaker;
        }

        public String getText() {
            return text;
        }

        public List<DialogueOptionDefinition> getOptions() {
            return options;
        }

        public Map<String, Object> getConditions() {
            return conditions;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class DialogueOptionDefinition {
        @JsonProperty("option_id")
        private String optionId;
        @JsonProperty("text")
        private String text;
        @JsonProperty("required_attribute")
        private AttributeRequirement requiredAttribute;
        @JsonProperty("skill_check")
        private SkillCheckRequirementDefinition skillCheck;
        @JsonProperty("leads_to_node")
        private String leadsToNode;
        @JsonProperty("effects")
        private Map<String, Object> effects;
        @JsonProperty("conditions")
        private Map<String, Object> conditions;
        @JsonProperty("show_when_locked")
        private Boolean showWhenLocked;
        @JsonProperty("branch_id")
        private String branchId;

        public String getOptionId() {
            return optionId;
        }

        public String getText() {
            return text;
        }

        public AttributeRequirement getRequiredAttribute() {
            return requiredAttribute;
        }

        public SkillCheckRequirementDefinition getSkillCheck() {
            return skillCheck;
        }

        public Optional<String> getSkillCheckOptional() {
            return skillCheck == null ? Optional.empty() : Optional.ofNullable(skillCheck.getSkill());
        }

        public String getLeadsToNode() {
            return leadsToNode;
        }

        public Map<String, Object> getEffects() {
            return effects;
        }

        public Map<String, Object> getConditions() {
            return conditions;
        }

        public Boolean getShowWhenLocked() {
            return showWhenLocked;
        }

        public String getBranchIdOrDefault(String defaultBranch) {
            return StringUtils.hasText(branchId) ? branchId : defaultBranch;
        }

        public Optional<String> getConsequenceOptional() {
            Object consequence = effects != null ? effects.get("consequence") : null;
            return Optional.ofNullable(consequence).map(Object::toString);
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class AttributeRequirement {
        @JsonProperty("attribute")
        private String attribute;
        @JsonProperty("min_value")
        private int minValue = 0;

        public String getAttribute() {
            return attribute;
        }

        public int getMinValue() {
            return minValue;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class SkillCheckRequirementDefinition {
        @JsonProperty("skill")
        private String skill;
        @JsonProperty("difficulty")
        private int difficulty;
        @JsonProperty("advantage")
        private boolean advantage;

        public String getSkill() {
            return skill;
        }

        public void setSkill(String skill) {
            this.skill = skill;
        }

        public int getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(int difficulty) {
            this.difficulty = difficulty;
        }

        public boolean isAdvantage() {
            return advantage;
        }

        public void setAdvantage(boolean advantage) {
            this.advantage = advantage;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class QuestRewardsDefinition {
        @JsonProperty("experience")
        private Integer experience;
        @JsonProperty("currency")
        private Integer currency;
        @JsonProperty("items")
        private List<RewardItemDefinition> items = new ArrayList<>();
        @JsonProperty("reputation")
        private Map<String, Integer> reputation = new HashMap<>();

        public QuestRewards toDto() {
            QuestRewards rewards = new QuestRewards()
                .experience(experience != null ? experience : 0)
                .currency(currency != null ? currency : 0);
            for (RewardItemDefinition item : items) {
                rewards.addItemsItem(new QuestRewardsItemsInner()
                    .itemId(item.getItemId())
                    .quantity(item.getQuantity()));
            }
            if (reputation != null) {
                rewards.setReputation(reputation);
            }
            return rewards;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class RewardItemDefinition {
        @JsonProperty("item_id")
        private String itemId;
        @JsonProperty("quantity")
        private Integer quantity;

        public String getItemId() {
            return itemId;
        }

        public Integer getQuantity() {
            return quantity;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ChoiceRecord {
        @JsonProperty("node_id")
        private String nodeId;
        @JsonProperty("option_id")
        private String optionId;
        @JsonProperty("next_node_id")
        private String nextNodeId;
        @JsonProperty("timestamp")
        private OffsetDateTime timestamp;

        @SuppressWarnings("unused")
        public ChoiceRecord() {
        }

        public ChoiceRecord(String nodeId, String optionId, String nextNodeId, OffsetDateTime timestamp) {
            this.nodeId = nodeId;
            this.optionId = optionId;
            this.nextNodeId = nextNodeId;
            this.timestamp = timestamp;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ProgressSnapshot {
        @JsonProperty("current")
        private int current;
        @JsonProperty("target")
        private int target = 1;
        @JsonProperty("completed")
        private boolean completed;

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getTarget() {
            return target;
        }

        public void setTarget(int target) {
            this.target = target;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }
    }
}


