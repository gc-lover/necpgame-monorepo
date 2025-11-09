package com.necpgame.adminservice.service;

import com.necpgame.adminservice.model.CalculateRomanceCompatibility200Response;
import com.necpgame.adminservice.model.CalculateRomanceCompatibilityRequest;
import com.necpgame.adminservice.model.DetermineRomanceTrigger200Response;
import com.necpgame.adminservice.model.DetermineRomanceTriggerRequest;
import com.necpgame.adminservice.model.GenerateDialogue200Response;
import com.necpgame.adminservice.model.GenerateDialogueRequest;
import com.necpgame.adminservice.model.GenerateNPCPersonalityRequest;
import com.necpgame.adminservice.model.GenerateRomanceDialogue200Response;
import com.necpgame.adminservice.model.GenerateRomanceDialogueRequest;
import com.necpgame.adminservice.model.GetNPCDecision200Response;
import com.necpgame.adminservice.model.GetNPCDecisionRequest;
import com.necpgame.adminservice.model.NPCPersonality;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for InternalService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface InternalService {

    /**
     * POST /internal/ai/romance/calculate-compatibility : Рассчитать совместимость для романа
     * Internal service call
     *
     * @param calculateRomanceCompatibilityRequest  (required)
     * @return CalculateRomanceCompatibility200Response
     */
    CalculateRomanceCompatibility200Response calculateRomanceCompatibility(CalculateRomanceCompatibilityRequest calculateRomanceCompatibilityRequest);

    /**
     * POST /internal/ai/romance/determine-trigger : Определить триггер романтического события
     * Когда должно произойти событие
     *
     * @param determineRomanceTriggerRequest  (required)
     * @return DetermineRomanceTrigger200Response
     */
    DetermineRomanceTrigger200Response determineRomanceTrigger(DetermineRomanceTriggerRequest determineRomanceTriggerRequest);

    /**
     * POST /internal/ai/dialogue/generate : Сгенерировать диалог
     *
     * @param generateDialogueRequest  (required)
     * @return GenerateDialogue200Response
     */
    GenerateDialogue200Response generateDialogue(GenerateDialogueRequest generateDialogueRequest);

    /**
     * POST /internal/ai/npc/generate-personality : Сгенерировать личность NPC
     *
     * @param generateNPCPersonalityRequest  (required)
     * @return NPCPersonality
     */
    NPCPersonality generateNPCPersonality(GenerateNPCPersonalityRequest generateNPCPersonalityRequest);

    /**
     * POST /internal/ai/romance/generate-dialogue : Сгенерировать романтический диалог
     *
     * @param generateRomanceDialogueRequest  (required)
     * @return GenerateRomanceDialogue200Response
     */
    GenerateRomanceDialogue200Response generateRomanceDialogue(GenerateRomanceDialogueRequest generateRomanceDialogueRequest);

    /**
     * POST /internal/ai/npc/decision : Получить AI решение NPC
     * Что NPC делает в ситуации
     *
     * @param getNPCDecisionRequest  (required)
     * @return GetNPCDecision200Response
     */
    GetNPCDecision200Response getNPCDecision(GetNPCDecisionRequest getNPCDecisionRequest);

    /**
     * GET /internal/ai/npc/personality/{npc_id} : Получить личность NPC
     *
     * @param npcId  (required)
     * @return NPCPersonality
     */
    NPCPersonality getNPCPersonality(String npcId);
}

