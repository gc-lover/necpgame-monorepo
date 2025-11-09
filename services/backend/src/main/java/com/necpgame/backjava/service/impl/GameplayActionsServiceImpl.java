package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.model.*;
import com.necpgame.backjava.service.GameplayActionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Р РµР°Р»РёР·Р°С†РёСЏ СЃРµСЂРІРёСЃР° РґР»СЏ РёРіСЂРѕРІС‹С… РґРµР№СЃС‚РІРёР№.
 * 
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/gameplay/actions/actions.yaml
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GameplayActionsServiceImpl implements GameplayActionsService {
    
    @Override
    @Transactional(readOnly = true)
    public ExploreLocation200Response exploreLocation(ExploreLocationRequest request) {
        log.info("Exploring location for character: {}", request.getCharacterId());
        return null; // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
    }
    
    @Override
    @Transactional
    public RestAction200Response restAction(RestActionRequest request) {
        log.info("Rest action for character: {}", request.getCharacterId());
        return null; // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
    }
    
    @Override
    @Transactional
    public UseObject200Response useObject(UseObjectRequest request) {
        log.info("Using object for character: {}", request.getCharacterId());
        return null; // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
    }
    
    @Override
    @Transactional
    public HackSystem200Response hackSystem(HackSystemRequest request) {
        log.info("Hacking system for character: {}", request.getCharacterId());
        return null; // TODO: РџРѕР»РЅР°СЏ СЂРµР°Р»РёР·Р°С†РёСЏ
    }
}

