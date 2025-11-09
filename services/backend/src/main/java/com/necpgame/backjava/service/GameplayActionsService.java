package com.necpgame.backjava.service;

import com.necpgame.backjava.model.*;

/**
 * GameplayActionsService - СЃРµСЂРІРёСЃ РґР»СЏ РёРіСЂРѕРІС‹С… РґРµР№СЃС‚РІРёР№.
 * 
 * РЎРіРµРЅРµСЂРёСЂРѕРІР°РЅРѕ РёР·: API-SWAGGER/api/v1/gameplay/actions/actions.yaml
 */
public interface GameplayActionsService {

    /**
     * РћСЃРјРѕС‚СЂРµС‚СЊСЃСЏ РІ Р»РѕРєР°С†РёРё.
     */
    ExploreLocation200Response exploreLocation(ExploreLocationRequest request);

    /**
     * РћС‚РґРѕС…РЅСѓС‚СЊ.
     */
    RestAction200Response restAction(RestActionRequest request);

    /**
     * РСЃРїРѕР»СЊР·РѕРІР°С‚СЊ РѕР±СЉРµРєС‚ РІ Р»РѕРєР°С†РёРё.
     */
    UseObject200Response useObject(UseObjectRequest request);

    /**
     * Р’Р·Р»РѕРјР°С‚СЊ СЃРёСЃС‚РµРјСѓ.
     */
    HackSystem200Response hackSystem(HackSystemRequest request);
}

