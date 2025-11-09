package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.WorldEventsApi;
import com.necpgame.backjava.model.EconomicMultipliers;
import com.necpgame.backjava.model.GetActiveWorldEvents200Response;
import com.necpgame.backjava.model.GetCharacterAffectedEvents200Response;
import com.necpgame.backjava.model.WorldEventDetailed;
import com.necpgame.backjava.service.WorldEventsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class WorldEventsController implements WorldEventsApi {

    private final WorldEventsService worldEventsService;

    public WorldEventsController(WorldEventsService worldEventsService) {
        this.worldEventsService = worldEventsService;
    }

    @Override
    public ResponseEntity<GetActiveWorldEvents200Response> getActiveWorldEvents(String era, String eventType, Integer page, Integer pageSize) {
        return ResponseEntity.ok(worldEventsService.getActiveWorldEvents(era, eventType, page, pageSize));
    }

    @Override
    public ResponseEntity<GetCharacterAffectedEvents200Response> getCharacterAffectedEvents(UUID characterId) {
        return ResponseEntity.ok(worldEventsService.getCharacterAffectedEvents(characterId));
    }

    @Override
    public ResponseEntity<EconomicMultipliers> getEconomicMultipliers() {
        return ResponseEntity.ok(worldEventsService.getEconomicMultipliers());
    }

    @Override
    public ResponseEntity<WorldEventDetailed> getWorldEvent(String eventId) {
        return ResponseEntity.ok(worldEventsService.getWorldEvent(eventId));
    }
}

