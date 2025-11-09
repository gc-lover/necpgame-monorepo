package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.LocationsApi;
import com.necpgame.backjava.model.GetCities200Response;
import com.necpgame.backjava.service.CitiesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST Controller для работы с городами.
 * 
 * Реализует контракт {@link LocationsApi}, сгенерированный из OpenAPI спецификации.
 * Источник: API-SWAGGER/api/v1/auth/character-creation-reference-models.yaml
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class CitiesController implements LocationsApi {
    
    private final CitiesService service;
    
    @Override
    public ResponseEntity<GetCities200Response> getCities(UUID factionId, String region) {
        log.info("GET /locations/cities?factionId={}&region={}", factionId, region);
        return ResponseEntity.ok(service.getCities(factionId, region));
    }
}

