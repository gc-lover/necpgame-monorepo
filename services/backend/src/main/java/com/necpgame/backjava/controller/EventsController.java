package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.EventsApi;
import com.necpgame.backjava.model.*;
import com.necpgame.backjava.service.EventsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST Controller РґР»СЏ СЂР°Р±РѕС‚С‹ СЃРѕ СЃР»СѓС‡Р°Р№РЅС‹РјРё СЃРѕР±С‹С‚РёСЏРјРё.
 * 
 * Р РµР°Р»РёР·СѓРµС‚ РєРѕРЅС‚СЂР°РєС‚ {@link EventsApi}, СЃРіРµРЅРµСЂРёСЂРѕРІР°РЅРЅС‹Р№ РёР· OpenAPI СЃРїРµС†РёС„РёРєР°С†РёРё.
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/events/random-events.yaml
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class EventsController implements EventsApi {
    
    private final EventsService service;
    
    @Override
    public ResponseEntity<RandomEvent> getRandomEvent(UUID characterId, String locationId, String context) {
        log.info("GET /events/random?characterId={}&locationId={}&context={}", characterId, locationId, context);
        return ResponseEntity.ok(service.getRandomEvent(characterId, locationId, context));
    }
    
    @Override
    public ResponseEntity<EventResult> respondToEvent(String eventId, RespondToEventRequest respondToEventRequest) {
        log.info("POST /events/{}/respond", eventId);
        return ResponseEntity.ok(service.respondToEvent(eventId, respondToEventRequest));
    }
    
    @Override
    public ResponseEntity<GetActiveEvents200Response> getActiveEvents(UUID characterId) {
        log.info("GET /events/active?characterId={}", characterId);
        return ResponseEntity.ok(service.getActiveEvents(characterId));
    }
}

