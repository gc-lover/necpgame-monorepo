package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.GameplayLocationsApi;
import com.necpgame.backjava.model.*;
import com.necpgame.backjava.service.LocationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST Controller РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РёРіСЂРѕРІС‹РјРё Р»РѕРєР°С†РёСЏРјРё.
 * 
 * Р РµР°Р»РёР·СѓРµС‚ РєРѕРЅС‚СЂР°РєС‚ {@link GameplayLocationsApi}, СЃРіРµРЅРµСЂРёСЂРѕРІР°РЅРЅС‹Р№ РёР· OpenAPI СЃРїРµС†РёС„РёРєР°С†РёРё.
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/locations/locations.yaml
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class LocationsController implements GameplayLocationsApi {
    
    private final LocationsService service;
    
    @Override
    public ResponseEntity<GetLocations200Response> getLocations(UUID characterId, String region, String dangerLevel, Integer minLevel) {
        log.info("GET /locations?characterId={}", characterId);
        return ResponseEntity.ok(service.getLocations(characterId, region, dangerLevel, minLevel));
    }
    
    @Override
    public ResponseEntity<LocationDetails> getLocationDetails(String locationId, UUID characterId) {
        log.info("GET /locations/{}?characterId={}", locationId, characterId);
        return ResponseEntity.ok(service.getLocationDetails(locationId, characterId));
    }
    
    @Override
    public ResponseEntity<LocationDetails> getCurrentLocation(UUID characterId) {
        log.info("GET /locations/current?characterId={}", characterId);
        return ResponseEntity.ok(service.getCurrentLocation(characterId));
    }
    
    @Override
    public ResponseEntity<TravelResponse> travelToLocation(TravelRequest travelRequest) {
        log.info("POST /locations/travel");
        return ResponseEntity.ok(service.travelToLocation(travelRequest));
    }
    
    @Override
    public ResponseEntity<GetLocationActions200Response> getLocationActions(String locationId, UUID characterId) {
        log.info("GET /locations/{}/actions?characterId={}", locationId, characterId);
        return ResponseEntity.ok(service.getLocationActions(locationId, characterId));
    }
    
    @Override
    public ResponseEntity<GetConnectedLocations200Response> getConnectedLocations(String locationId, UUID characterId) {
        log.info("GET /locations/{}/connected?characterId={}", locationId, characterId);
        return ResponseEntity.ok(service.getConnectedLocations(locationId, characterId));
    }
}
