package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.GameplayActionsApi;
import com.necpgame.backjava.model.*;
import com.necpgame.backjava.service.GameplayActionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller РґР»СЏ РёРіСЂРѕРІС‹С… РґРµР№СЃС‚РІРёР№.
 * 
 * Р РµР°Р»РёР·СѓРµС‚ РєРѕРЅС‚СЂР°РєС‚ {@link GameplayActionsApi}, СЃРіРµРЅРµСЂРёСЂРѕРІР°РЅРЅС‹Р№ РёР· OpenAPI.
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/actions/actions.yaml
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class GameplayActionsController implements GameplayActionsApi {
    
    private final GameplayActionsService service;
    
    @Override
    public ResponseEntity<ExploreLocation200Response> exploreLocation(ExploreLocationRequest exploreLocationRequest) {
        log.info("POST /gameplay/actions/explore");
        return ResponseEntity.ok(service.exploreLocation(exploreLocationRequest));
    }
    
    @Override
    public ResponseEntity<RestAction200Response> restAction(RestActionRequest restActionRequest) {
        log.info("POST /gameplay/actions/rest");
        return ResponseEntity.ok(service.restAction(restActionRequest));
    }
    
    @Override
    public ResponseEntity<UseObject200Response> useObject(UseObjectRequest useObjectRequest) {
        log.info("POST /gameplay/actions/use");
        return ResponseEntity.ok(service.useObject(useObjectRequest));
    }
    
    @Override
    public ResponseEntity<HackSystem200Response> hackSystem(HackSystemRequest hackSystemRequest) {
        log.info("POST /gameplay/actions/hack");
        return ResponseEntity.ok(service.hackSystem(hackSystemRequest));
    }
}

