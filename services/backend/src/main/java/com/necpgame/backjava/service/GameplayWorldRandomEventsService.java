package com.necpgame.backjava.service;

import com.necpgame.backjava.model.Error;
import com.necpgame.backjava.model.EventResolutionResult;
import com.necpgame.backjava.model.GenerateEventForLocation200Response;
import com.necpgame.backjava.model.GenerateEventForLocationRequest;
import com.necpgame.backjava.model.GetActiveEvents200Response;
import com.necpgame.backjava.model.GetEventHistory200Response;
import com.necpgame.backjava.model.ListRandomEvents200Response;
import org.springframework.lang.Nullable;
import com.necpgame.backjava.model.RandomEventDetailed;
import com.necpgame.backjava.model.ResolveEventRequest;
import com.necpgame.backjava.model.TriggerEventRequest;
import com.necpgame.backjava.model.TriggeredEventInstance;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for GameplayWorldRandomEventsService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface GameplayWorldRandomEventsService {

    /**
     * POST /gameplay/world/random-events/generate : Сгенерировать событие для локации
     * Backend вызывает при перемещении игрока
     *
     * @param generateEventForLocationRequest  (required)
     * @return GenerateEventForLocation200Response
     */
    GenerateEventForLocation200Response generateEventForLocation(GenerateEventForLocationRequest generateEventForLocationRequest);

    /**
     * GET /gameplay/world/random-events/character/{character_id}/active : Получить активные события для персонажа
     *
     * @param characterId  (required)
     * @return GetActiveEvents200Response
     */
    GetActiveEvents200Response getActiveEvents(UUID characterId);

    /**
     * GET /gameplay/world/random-events/character/{character_id}/history : Получить историю случайных событий
     *
     * @param characterId  (required)
     * @param period  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return GetEventHistory200Response
     */
    GetEventHistory200Response getEventHistory(UUID characterId, String period, Integer page, Integer pageSize);

    /**
     * GET /gameplay/world/random-events/{event_id} : Получить детали события
     *
     * @param eventId  (required)
     * @return RandomEventDetailed
     */
    RandomEventDetailed getRandomEvent(String eventId);

    /**
     * GET /gameplay/world/random-events : Получить список случайных событий
     *
     * @param period  (optional)
     * @param category  (optional)
     * @param locationType Тип локации где событие может произойти (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return ListRandomEvents200Response
     */
    ListRandomEvents200Response listRandomEvents(String period, String category, String locationType, Integer page, Integer pageSize);

    /**
     * POST /gameplay/world/random-events/character/{character_id}/resolve : Разрешить событие (сделать выбор)
     *
     * @param characterId  (required)
     * @param resolveEventRequest  (required)
     * @return EventResolutionResult
     */
    EventResolutionResult resolveEvent(UUID characterId, ResolveEventRequest resolveEventRequest);

    /**
     * POST /gameplay/world/random-events/trigger : Триггернуть случайное событие
     * Используется backend системами для генерации событий
     *
     * @param triggerEventRequest  (required)
     * @return TriggeredEventInstance
     */
    TriggeredEventInstance triggerRandomEvent(TriggerEventRequest triggerEventRequest);
}

