package com.necpgame.backjava.service;

import com.necpgame.backjava.model.*;

import java.util.UUID;

/**
 * CharactersStatusService - СЃРµСЂРІРёСЃ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃРѕ СЃС‚Р°С‚СѓСЃРѕРј Рё С…Р°СЂР°РєС‚РµСЂРёСЃС‚РёРєР°РјРё РїРµСЂСЃРѕРЅР°Р¶Р°.
 * 
 * РЎРіРµРЅРµСЂРёСЂРѕРІР°РЅРѕ РЅР° РѕСЃРЅРѕРІРµ: API-SWAGGER/api/v1/characters/status.yaml
 */
public interface CharactersStatusService {

    /**
     * РџРѕР»СѓС‡РёС‚СЊ СЃС‚Р°С‚СѓСЃ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    CharacterStatus getCharacterStatus(UUID characterId);

    /**
     * РџРѕР»СѓС‡РёС‚СЊ С…Р°СЂР°РєС‚РµСЂРёСЃС‚РёРєРё РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    CharacterStats getCharacterStats(UUID characterId);

    /**
     * РџРѕР»СѓС‡РёС‚СЊ РЅР°РІС‹РєРё РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    GetCharacterSkills200Response getCharacterSkills(UUID characterId);

    /**
     * РћР±РЅРѕРІРёС‚СЊ СЃС‚Р°С‚СѓСЃ РїРµСЂСЃРѕРЅР°Р¶Р° (Р·РґРѕСЂРѕРІСЊРµ, СЌРЅРµСЂРіРёСЏ, С‡РµР»РѕРІРµС‡РЅРѕСЃС‚СЊ, РѕРїС‹С‚).
     */
    CharacterStatus updateCharacterStatus(UUID characterId, UpdateCharacterStatusRequest request);
}

