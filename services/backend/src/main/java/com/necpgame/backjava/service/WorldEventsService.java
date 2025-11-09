package com.necpgame.backjava.service;

import com.necpgame.backjava.model.EconomicMultipliers;
import com.necpgame.backjava.model.Error;
import com.necpgame.backjava.model.GetActiveWorldEvents200Response;
import com.necpgame.backjava.model.GetCharacterAffectedEvents200Response;
import org.springframework.lang.Nullable;
import java.util.UUID;
import com.necpgame.backjava.model.WorldEventDetailed;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for WorldEventsService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface WorldEventsService {

    /**
     * GET /gameplay/world/events : Получить активные мировые события
     *
     * @param era  (optional)
     * @param eventType  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return GetActiveWorldEvents200Response
     */
    GetActiveWorldEvents200Response getActiveWorldEvents(String era, String eventType, Integer page, Integer pageSize);

    /**
     * GET /gameplay/world/events/character/{character_id}/affected : Получить события, влияющие на персонажа
     *
     * @param characterId  (required)
     * @return GetCharacterAffectedEvents200Response
     */
    GetCharacterAffectedEvents200Response getCharacterAffectedEvents(UUID characterId);

    /**
     * GET /gameplay/world/events/economy/multipliers : Получить экономические множители от событий
     *
     * @return EconomicMultipliers
     */
    EconomicMultipliers getEconomicMultipliers();

    /**
     * GET /gameplay/world/events/{event_id} : Получить детали мирового события
     *
     * @param eventId  (required)
     * @return WorldEventDetailed
     */
    WorldEventDetailed getWorldEvent(String eventId);
}

