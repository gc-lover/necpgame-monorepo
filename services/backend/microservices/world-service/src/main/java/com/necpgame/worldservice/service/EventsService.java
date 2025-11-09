package com.necpgame.worldservice.service;

import com.necpgame.worldservice.model.EventResult;
import com.necpgame.worldservice.model.GetActiveEvents200Response;
import org.springframework.lang.Nullable;
import com.necpgame.worldservice.model.RandomEvent;
import com.necpgame.worldservice.model.RespondToEventRequest;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for EventsService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface EventsService {

    /**
     * GET /events/active : Активные события
     *
     * @param characterId  (required)
     * @return GetActiveEvents200Response
     */
    GetActiveEvents200Response getActiveEvents(UUID characterId);

    /**
     * GET /events/random : Получить случайное событие
     *
     * @param characterId  (required)
     * @param locationId  (optional)
     * @param context  (optional)
     * @return RandomEvent
     */
    RandomEvent getRandomEvent(UUID characterId, String locationId, String context);

    /**
     * POST /events/{eventId}/respond : Ответить на событие
     *
     * @param eventId  (required)
     * @param respondToEventRequest  (optional)
     * @return EventResult
     */
    EventResult respondToEvent(String eventId, RespondToEventRequest respondToEventRequest);
}

