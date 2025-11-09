package com.necpgame.backjava.service;

import com.necpgame.backjava.model.CompleteQuestRequest;
import com.necpgame.backjava.model.DialogueChoiceRequest;
import com.necpgame.backjava.model.DialogueChoiceResult;
import com.necpgame.backjava.model.DialogueNode;
import com.necpgame.backjava.model.DialogueTree;
import com.necpgame.backjava.model.FactionQuestDetailed;
import com.necpgame.backjava.model.GetActiveQuests200Response;
import com.necpgame.backjava.model.GetAvailableFactionQuests200Response;
import com.necpgame.backjava.model.GetFactionQuestProgress200Response;
import com.necpgame.backjava.model.GetQuestCatalog200Response;
import com.necpgame.backjava.model.GetQuestBranches200Response;
import com.necpgame.backjava.model.GetQuestChains200Response;
import com.necpgame.backjava.model.GetQuestEndings200Response;
import com.necpgame.backjava.model.GetQuestRecommendations200Response;
import com.necpgame.backjava.model.ListFactionQuests200Response;
import com.necpgame.backjava.model.QuestCompletionResult;
import com.necpgame.backjava.model.QuestInstance;
import com.necpgame.backjava.model.QuestDetails;
import com.necpgame.backjava.model.QuestLootTable;
import com.necpgame.backjava.model.SearchQuests200Response;
import com.necpgame.backjava.model.SkillCheckRequest;
import com.necpgame.backjava.model.SkillCheckResult;
import com.necpgame.backjava.model.StartQuestRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for NarrativeService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface NarrativeService {

    GetQuestCatalog200Response getQuestCatalog(
        @Nullable String type,
        @Nullable String period,
        @Nullable String difficulty,
        @Nullable String faction,
        @Nullable Integer minLevel,
        @Nullable Integer maxLevel,
        @Nullable Boolean hasRomance,
        @Nullable Boolean hasCombat,
        @Nullable Integer estimatedTimeMin,
        @Nullable Integer estimatedTimeMax,
        @Nullable Integer page,
        @Nullable Integer pageSize
    );

    SearchQuests200Response searchQuests(String query, @Nullable List<String> searchIn, @Nullable Integer page, @Nullable Integer pageSize);

    QuestDetails getQuestDetails(String questId);

    DialogueTree getQuestDialogueTree(String questId);

    QuestLootTable getQuestLootTable(String questId);

    GetQuestRecommendations200Response getQuestRecommendations(UUID characterId, @Nullable Integer count);

    GetQuestChains200Response getQuestChains(@Nullable String faction, @Nullable String storyline);

    /**
     * GET /narrative/faction-quests : Получить список фракционных квестов
     *
     * @param faction  (optional)
     * @param minReputation Минимальная репутация для доступа (optional)
     * @param playerLevelMin  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return ListFactionQuests200Response
     */
    ListFactionQuests200Response listFactionQuests(String faction, Integer minReputation, Integer playerLevelMin, Integer page, Integer pageSize);

    /**
     * GET /narrative/faction-quests/{quest_id} : Получить детали фракционного квеста
     *
     * @param questId  (required)
     * @return FactionQuestDetailed
     */
    FactionQuestDetailed getFactionQuest(String questId);

    /**
     * GET /narrative/faction-quests/{quest_id}/branches : Получить ветвления квеста
     *
     * @param questId  (required)
     * @return GetQuestBranches200Response
     */
    GetQuestBranches200Response getQuestBranches(String questId);

    /**
     * GET /narrative/faction-quests/{quest_id}/endings : Получить возможные концовки квеста
     * Может быть 12+ концовок в зависимости от выборов
     *
     * @param questId  (required)
     * @return GetQuestEndings200Response
     */
    GetQuestEndings200Response getQuestEndings(String questId);

    /**
     * GET /narrative/faction-quests/character/{character_id}/available : Получить доступные фракционные квесты для персонажа
     *
     * @param characterId  (required)
     * @return GetAvailableFactionQuests200Response
     */
    GetAvailableFactionQuests200Response getAvailableFactionQuests(UUID characterId);

    /**
     * GET /narrative/faction-quests/character/{character_id}/progress : Получить прогресс по фракционным квестам
     *
     * @param characterId  (required)
     * @return GetFactionQuestProgress200Response
     */
    GetFactionQuestProgress200Response getFactionQuestProgress(UUID characterId);

    /**
     * POST /narrative/quest-engine/instances/{instance_id}/abandon : Отказаться от квеста
     *
     * @param instanceId  (required)
     * @return Void
     */
    Void abandonQuest(UUID instanceId);

    /**
     * POST /narrative/quest-engine/instances/{instance_id}/dialogue/choose : Выбрать вариант диалога
     *
     * @param instanceId  (required)
     * @param dialogueChoiceRequest  (required)
     * @return DialogueChoiceResult
     */
    DialogueChoiceResult chooseDialogueOption(UUID instanceId, DialogueChoiceRequest dialogueChoiceRequest);

    /**
     * POST /narrative/quest-engine/instances/{instance_id}/complete : Завершить квест
     *
     * @param instanceId  (required)
     * @param completeQuestRequest  (required)
     * @return QuestCompletionResult
     */
    QuestCompletionResult completeQuest(UUID instanceId, CompleteQuestRequest completeQuestRequest);

    /**
     * GET /narrative/quest-engine/character/{character_id}/active : Получить активные квесты персонажа
     *
     * @param characterId  (required)
     * @return GetActiveQuests200Response
     */
    GetActiveQuests200Response getActiveQuests(UUID characterId);

    /**
     * GET /narrative/quest-engine/instances/{instance_id}/dialogue : Получить текущий диалог
     *
     * @param instanceId  (required)
     * @return DialogueNode
     */
    DialogueNode getCurrentDialogue(UUID instanceId);

    /**
     * GET /narrative/quest-engine/instances/{instance_id} : Получить состояние квеста
     *
     * @param instanceId  (required)
     * @return QuestInstance
     */
    QuestInstance getQuestInstance(UUID instanceId);

    /**
     * POST /narrative/quest-engine/instances/{instance_id}/skill-check : Выполнить skill check
     * D&amp;D skill check (бросок d20 + модификатор)
     *
     * @param instanceId  (required)
     * @param skillCheckRequest  (required)
     * @return SkillCheckResult
     */
    SkillCheckResult performSkillCheck(UUID instanceId, SkillCheckRequest skillCheckRequest);

    /**
     * POST /narrative/quest-engine/start : Начать новый квест
     *
     * @param startQuestRequest  (required)
     * @return QuestInstance
     */
    QuestInstance startQuest(StartQuestRequest startQuestRequest);
}

