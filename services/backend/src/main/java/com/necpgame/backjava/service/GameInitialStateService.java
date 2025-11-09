package com.necpgame.backjava.service;

import com.necpgame.backjava.model.*;

import java.util.UUID;

/**
 * GameInitialStateService - СЃРµСЂРІРёСЃ РґР»СЏ РїРѕР»СѓС‡РµРЅРёСЏ РЅР°С‡Р°Р»СЊРЅРѕРіРѕ СЃРѕСЃС‚РѕСЏРЅРёСЏ РёРіСЂС‹.
 * 
 * РЎРіРµРЅРµСЂРёСЂРѕРІР°РЅРѕ РёР·: API-SWAGGER/api/v1/game/initial-state.yaml
 * 
 * РќР• СЂРµРґР°РєС‚РёСЂСѓР№С‚Рµ СЌС‚РѕС‚ С„Р°Р№Р» РІСЂСѓС‡РЅСѓСЋ - РѕРЅ РіРµРЅРµСЂРёСЂСѓРµС‚СЃСЏ Р°РІС‚РѕРјР°С‚РёС‡РµСЃРєРё!
 */
public interface GameInitialStateService {

    /**
     * РџРѕР»СѓС‡РёС‚СЊ РЅР°С‡Р°Р»СЊРЅРѕРµ СЃРѕСЃС‚РѕСЏРЅРёРµ РёРіСЂС‹ РґР»СЏ РїРµСЂСЃРѕРЅР°Р¶Р°.
     * 
     * @param characterId ID РїРµСЂСЃРѕРЅР°Р¶Р°
     * @return РЅР°С‡Р°Р»СЊРЅРѕРµ СЃРѕСЃС‚РѕСЏРЅРёРµ РёРіСЂС‹
     */
    InitialStateResponse getInitialState(UUID characterId);

    /**
     * РџРѕР»СѓС‡РёС‚СЊ С€Р°РіРё С‚СѓС‚РѕСЂРёР°Р»Р° РґР»СЏ РЅРѕРІРѕРіРѕ РёРіСЂРѕРєР°.
     * 
     * @param characterId ID РїРµСЂСЃРѕРЅР°Р¶Р°
     * @return С€Р°РіРё С‚СѓС‚РѕСЂРёР°Р»Р°
     */
    TutorialStepsResponse getTutorialSteps(UUID characterId);
}

