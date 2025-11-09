package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.EventGenerationApi;
import com.necpgame.backjava.model.GenerateEventRequest;
import com.necpgame.backjava.model.GeneratedEvent;
import com.necpgame.backjava.service.EventGenerationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EventGenerationController implements EventGenerationApi {

    private final EventGenerationService eventGenerationService;

    public EventGenerationController(EventGenerationService eventGenerationService) {
        this.eventGenerationService = eventGenerationService;
    }

    @Override
    public ResponseEntity<GeneratedEvent> generateWorldEvent(GenerateEventRequest generateEventRequest) {
        return ResponseEntity.ok(eventGenerationService.generateWorldEvent(generateEventRequest));
    }
}

