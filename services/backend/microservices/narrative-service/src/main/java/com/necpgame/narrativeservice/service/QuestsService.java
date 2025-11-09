package com.necpgame.narrativeservice.service;

import com.necpgame.narrativeservice.model.AbandonQuest200Response;
import com.necpgame.narrativeservice.model.AbandonQuestRequest;
import com.necpgame.narrativeservice.model.AcceptQuest200Response;
import com.necpgame.narrativeservice.model.AcceptQuestRequest;
import com.necpgame.narrativeservice.model.CompleteQuest200Response;
import com.necpgame.narrativeservice.model.CompleteQuestRequest;
import com.necpgame.narrativeservice.model.Error;
import com.necpgame.narrativeservice.model.GetActiveQuests200Response;
import com.necpgame.narrativeservice.model.GetAvailableQuests200Response;
import com.necpgame.narrativeservice.model.GetQuestObjectives200Response;
import org.springframework.lang.Nullable;
import com.necpgame.narrativeservice.model.Quest;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for QuestsService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface QuestsService {

    /**
     * POST /quests/abandon : Отказаться от квеста
     * Отказывается от активного квеста (не завершая его).
     *
     * @param abandonQuestRequest  (required)
     * @return AbandonQuest200Response
     */
    AbandonQuest200Response abandonQuest(AbandonQuestRequest abandonQuestRequest);

    /**
     * POST /quests/accept : Принять квест
     * Принимает квест от NPC или из списка доступных.
     *
     * @param acceptQuestRequest  (required)
     * @return AcceptQuest200Response
     */
    AcceptQuest200Response acceptQuest(AcceptQuestRequest acceptQuestRequest);

    /**
     * POST /quests/complete : Завершить квест
     * Завершает квест и выдает награды.
     *
     * @param completeQuestRequest  (required)
     * @return CompleteQuest200Response
     */
    CompleteQuest200Response completeQuest(CompleteQuestRequest completeQuestRequest);

    /**
     * GET /quests/active : Активные квесты персонажа
     * Возвращает список активных (принятых, но не завершенных) квестов.
     *
     * @param characterId  (required)
     * @return GetActiveQuests200Response
     */
    GetActiveQuests200Response getActiveQuests(UUID characterId);

    /**
     * GET /quests : Список доступных квестов
     * Возвращает список всех доступных квестов для персонажа (не принятых).
     *
     * @param characterId  (required)
     * @param type  (optional)
     * @return GetAvailableQuests200Response
     */
    GetAvailableQuests200Response getAvailableQuests(UUID characterId, String type);

    /**
     * GET /quests/{questId} : Детали квеста
     * Возвращает детальную информацию о квесте.
     *
     * @param questId  (required)
     * @param characterId  (required)
     * @return Quest
     */
    Quest getQuestDetails(String questId, UUID characterId);

    /**
     * GET /quests/{questId}/objectives : Цели квеста
     * Возвращает список целей (objectives) квеста с прогрессом.
     *
     * @param questId  (required)
     * @param characterId  (required)
     * @return GetQuestObjectives200Response
     */
    GetQuestObjectives200Response getQuestObjectives(String questId, UUID characterId);
}

