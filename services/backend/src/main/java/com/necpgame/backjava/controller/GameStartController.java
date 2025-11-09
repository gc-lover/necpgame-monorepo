package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.GameStartApi;
import com.necpgame.backjava.model.*;
import com.necpgame.backjava.service.GameStartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * GameStartController - REST РєРѕРЅС‚СЂРѕР»Р»РµСЂ РґР»СЏ Р·Р°РїСѓСЃРєР° РёРіСЂС‹.
 * 
 * Р РµР°Р»РёР·СѓРµС‚ API РёРЅС‚РµСЂС„РµР№СЃ GameStartApi.
 * Р’СЃРµ Spring MVC Р°РЅРЅРѕС‚Р°С†РёРё РѕРїСЂРµРґРµР»РµРЅС‹ РІ РёРЅС‚РµСЂС„РµР№СЃРµ.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class GameStartController implements GameStartApi {

    private final GameStartService gameStartService;

    @Override
    public ResponseEntity<GameStartResponse> startGame(GameStartRequest body) {
        log.info("POST /v1/game/start - Starting game for character: {}", body.getCharacterId());
        GameStartResponse response = gameStartService.startGame(body);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<WelcomeScreenResponse> getWelcomeScreen(UUID characterId) {
        log.info("GET /v1/game/welcome - Getting welcome screen for character: {}", characterId);
        WelcomeScreenResponse response = gameStartService.getWelcomeScreen(characterId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GameReturnResponse> returnToGame(GameReturnRequest body) {
        log.info("POST /v1/game/return - Returning to game for character: {}", body.getCharacterId());
        GameReturnResponse response = gameStartService.returnToGame(body);
        return ResponseEntity.ok(response);
    }
}

