package com.necpgame.backjava.service;

import com.necpgame.backjava.model.*;

import java.util.UUID;

/**
 * EventsService - СЃРµСЂРІРёСЃ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃРѕ СЃР»СѓС‡Р°Р№РЅС‹РјРё СЃРѕР±С‹С‚РёСЏРјРё.
 * 
 * РЎРіРµРЅРµСЂРёСЂРѕРІР°РЅРѕ РЅР° РѕСЃРЅРѕРІРµ: API-SWAGGER/api/v1/events/random-events.yaml
 */
public interface EventsService {

    /**
     * РџРѕР»СѓС‡РёС‚СЊ СЃР»СѓС‡Р°Р№РЅРѕРµ СЃРѕР±С‹С‚РёРµ.
     */
    RandomEvent getRandomEvent(UUID characterId, String locationId, String context);

    /**
     * РћС‚РІРµС‚РёС‚СЊ РЅР° СЃРѕР±С‹С‚РёРµ (РІС‹Р±СЂР°С‚СЊ РѕРїС†РёСЋ).
     */
    EventResult respondToEvent(String eventId, RespondToEventRequest request);

    /**
     * РџРѕР»СѓС‡РёС‚СЊ СЃРїРёСЃРѕРє Р°РєС‚РёРІРЅС‹С… СЃРѕР±С‹С‚РёР№ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    GetActiveEvents200Response getActiveEvents(UUID characterId);
}

