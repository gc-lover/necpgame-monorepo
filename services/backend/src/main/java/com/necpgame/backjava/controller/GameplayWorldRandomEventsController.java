package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.GameplayWorldRandomEventsApi;
import com.necpgame.backjava.model.EventResolutionResult;
import com.necpgame.backjava.model.GenerateEventForLocation200Response;
import com.necpgame.backjava.model.GenerateEventForLocationRequest;
import com.necpgame.backjava.model.GetActiveEvents200Response;
import com.necpgame.backjava.model.GetEventHistory200Response;
import com.necpgame.backjava.model.ListRandomEvents200Response;
import com.necpgame.backjava.model.RandomEventDetailed;
import com.necpgame.backjava.model.ResolveEventRequest;
import com.necpgame.backjava.model.TriggerEventRequest;
import com.necpgame.backjava.model.TriggeredEventInstance;
import com.necpgame.backjava.service.GameplayWorldRandomEventsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class GameplayWorldRandomEventsController implements GameplayWorldRandomEventsApi {

    private final GameplayWorldRandomEventsService randomEventsService;

    @Override
    public ResponseEntity<ListRandomEvents200Response> listRandomEvents(@Nullable String period,
                                                                       @Nullable String category,
                                                                       @Nullable String locationType,
                                                                       Integer page,
                                                                       Integer pageSize) {
        log.info("GET /gameplay/world/random-events [period={}, category={}, locationType={}, page={}, pageSize={}]",
            period, category, locationType, page, pageSize);
        return ResponseEntity.ok(randomEventsService.listRandomEvents(period, category, locationType, page, pageSize));
    }

    @Override
    public ResponseEntity<RandomEventDetailed> getRandomEvent(String eventId) {
        log.info("GET /gameplay/world/random-events/{}", eventId);
        return ResponseEntity.ok(randomEventsService.getRandomEvent(eventId));
    }

    @Override
    public ResponseEntity<TriggeredEventInstance> triggerRandomEvent(TriggerEventRequest triggerEventRequest) {
        log.info("POST /gameplay/world/random-events/trigger [eventId={}, characterId={}]",
            triggerEventRequest.getEventId(), triggerEventRequest.getCharacterId());
        return ResponseEntity.ok(randomEventsService.triggerRandomEvent(triggerEventRequest));
    }

    @Override
    public ResponseEntity<GetActiveEvents200Response> getActiveEvents(UUID characterId) {
        log.info("GET /gameplay/world/random-events/character/{}/active", characterId);
        return ResponseEntity.ok(randomEventsService.getActiveEvents(characterId));
    }

    @Override
    public ResponseEntity<EventResolutionResult> resolveEvent(UUID characterId, ResolveEventRequest resolveEventRequest) {
        log.info("POST /gameplay/world/random-events/character/{}/resolve [instanceId={}]",
            characterId, resolveEventRequest.getInstanceId());
        return ResponseEntity.ok(randomEventsService.resolveEvent(characterId, resolveEventRequest));
    }

    @Override
    public ResponseEntity<GetEventHistory200Response> getEventHistory(UUID characterId,
                                                                      @Nullable String period,
                                                                      Integer page,
                                                                      Integer pageSize) {
        log.info("GET /gameplay/world/random-events/character/{}/history [period={}, page={}, pageSize={}]",
            characterId, period, page, pageSize);
        return ResponseEntity.ok(randomEventsService.getEventHistory(characterId, period, page, pageSize));
    }

    @Override
    public ResponseEntity<GenerateEventForLocation200Response> generateEventForLocation(GenerateEventForLocationRequest generateEventForLocationRequest) {
        log.info("POST /gameplay/world/random-events/generate [characterId={}, locationId={}]",
            generateEventForLocationRequest.getCharacterId(), generateEventForLocationRequest.getLocationId());
        return ResponseEntity.ok(randomEventsService.generateEventForLocation(generateEventForLocationRequest));
    }
}

