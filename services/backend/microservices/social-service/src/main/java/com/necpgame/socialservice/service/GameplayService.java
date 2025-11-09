package com.necpgame.socialservice.service;

import com.necpgame.socialservice.model.BreakupRelationship200Response;
import com.necpgame.socialservice.model.BreakupRelationshipRequest;
import com.necpgame.socialservice.model.CalculateCompatibilityRequest;
import com.necpgame.socialservice.model.CompatibilityResult;
import com.necpgame.socialservice.model.Error;
import com.necpgame.socialservice.model.GetAvailableRomanceEvents200Response;
import com.necpgame.socialservice.model.GetAvailableRomanceNPCs200Response;
import com.necpgame.socialservice.model.GetRomanceRelationships200Response;
import com.necpgame.socialservice.model.MakeRomanceChoiceRequest;
import org.springframework.lang.Nullable;
import com.necpgame.socialservice.model.ProgressRelationship200Response;
import com.necpgame.socialservice.model.RomanceChoiceResult;
import com.necpgame.socialservice.model.RomanceEventInstance;
import com.necpgame.socialservice.model.RomanceRelationshipDetailed;
import com.necpgame.socialservice.model.TriggerRomanceEventRequest;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for GameplayService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface GameplayService {

    /**
     * POST /gameplay/social/romance/relationship/{relationship_id}/breakup : Расстаться с NPC
     *
     * @param relationshipId  (required)
     * @param breakupRelationshipRequest  (required)
     * @return BreakupRelationship200Response
     */
    BreakupRelationship200Response breakupRelationship(UUID relationshipId, BreakupRelationshipRequest breakupRelationshipRequest);

    /**
     * POST /gameplay/social/romance/compatibility : Рассчитать совместимость с NPC
     *
     * @param calculateCompatibilityRequest  (required)
     * @return CompatibilityResult
     */
    CompatibilityResult calculateCompatibility(CalculateCompatibilityRequest calculateCompatibilityRequest);

    /**
     * GET /gameplay/social/romance/events : Получить доступные романтические события
     *
     * @param characterId  (required)
     * @param relationshipId  (required)
     * @return GetAvailableRomanceEvents200Response
     */
    GetAvailableRomanceEvents200Response getAvailableRomanceEvents(UUID characterId, UUID relationshipId);

    /**
     * GET /gameplay/social/romance/available-npcs : Получить доступных NPC для романов
     *
     * @param characterId  (required)
     * @param region  (optional)
     * @param minCompatibility Минимальная совместимость (%) (optional, default to 50)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return GetAvailableRomanceNPCs200Response
     */
    GetAvailableRomanceNPCs200Response getAvailableRomanceNPCs(UUID characterId, String region, Integer minCompatibility, Integer page, Integer pageSize);

    /**
     * GET /gameplay/social/romance/relationship/{relationship_id} : Получить детали романтических отношений
     *
     * @param relationshipId  (required)
     * @return RomanceRelationshipDetailed
     */
    RomanceRelationshipDetailed getRomanceRelationship(UUID relationshipId);

    /**
     * GET /gameplay/social/romance/character/{character_id}/relationships : Получить активные романтические отношения
     *
     * @param characterId  (required)
     * @return GetRomanceRelationships200Response
     */
    GetRomanceRelationships200Response getRomanceRelationships(UUID characterId);

    /**
     * POST /gameplay/social/romance/events/{instance_id}/choice : Сделать выбор в романтическом событии
     *
     * @param instanceId  (required)
     * @param makeRomanceChoiceRequest  (required)
     * @return RomanceChoiceResult
     */
    RomanceChoiceResult makeRomanceChoice(UUID instanceId, MakeRomanceChoiceRequest makeRomanceChoiceRequest);

    /**
     * POST /gameplay/social/romance/relationship/{relationship_id}/progress : Развить отношения
     * Переход на следующую стадию
     *
     * @param relationshipId  (required)
     * @return ProgressRelationship200Response
     */
    ProgressRelationship200Response progressRelationship(UUID relationshipId);

    /**
     * POST /gameplay/social/romance/events/{event_id}/trigger : Запустить романтическое событие
     *
     * @param eventId  (required)
     * @param triggerRomanceEventRequest  (required)
     * @return RomanceEventInstance
     */
    RomanceEventInstance triggerRomanceEvent(String eventId, TriggerRomanceEventRequest triggerRomanceEventRequest);
}

