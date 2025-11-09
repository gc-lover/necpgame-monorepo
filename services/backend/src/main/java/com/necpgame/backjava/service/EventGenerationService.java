package com.necpgame.backjava.service;

import com.necpgame.backjava.model.Error;
import com.necpgame.backjava.model.GenerateEventRequest;
import com.necpgame.backjava.model.GeneratedEvent;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for EventGenerationService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface EventGenerationService {

    /**
     * POST /gameplay/world/events/generate : Сгенерировать событие
     * Используется backend для генерации случайных событий (d100)
     *
     * @param generateEventRequest  (required)
     * @return GeneratedEvent
     */
    GeneratedEvent generateWorldEvent(GenerateEventRequest generateEventRequest);
}

