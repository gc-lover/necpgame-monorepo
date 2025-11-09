package com.necpgame.backjava.service;

import com.necpgame.backjava.model.*;

import java.util.UUID;

/**
 * QuestsService - СЃРµСЂРІРёСЃ РґР»СЏ СѓРїСЂР°РІР»РµРЅРёСЏ РєРІРµСЃС‚РѕРІРѕР№ СЃРёСЃС‚РµРјРѕР№.
 * 
 * РЎРіРµРЅРµСЂРёСЂРѕРІР°РЅРѕ РёР·: API-SWAGGER/api/v1/quests/quests.yaml
 */
public interface QuestsService {

    /**
     * РџРѕР»СѓС‡РёС‚СЊ СЃРїРёСЃРѕРє РґРѕСЃС‚СѓРїРЅС‹С… РєРІРµСЃС‚РѕРІ.
     */
    GetAvailableQuests200Response getAvailableQuests(UUID characterId, String type);

    /**
     * РџРѕР»СѓС‡РёС‚СЊ Р°РєС‚РёРІРЅС‹Рµ РєРІРµСЃС‚С‹ РїРµСЂСЃРѕРЅР°Р¶Р°.
     */
    GetActiveQuests200Response getActiveQuests(UUID characterId);

    /**
     * РџРѕР»СѓС‡РёС‚СЊ РґРµС‚Р°Р»Рё РєРІРµСЃС‚Р°.
     */
    Quest getQuestDetails(String questId, UUID characterId);

    /**
     * РџСЂРёРЅСЏС‚СЊ РєРІРµСЃС‚.
     */
    AcceptQuest200Response acceptQuest(String questId, AcceptQuestRequest request);

    /**
     * Р—Р°РІРµСЂС€РёС‚СЊ РєРІРµСЃС‚.
     */
    CompleteQuest200Response completeQuest(String questId, CompleteQuestRequest request);

    /**
     * РћС‚РєР°Р·Р°С‚СЊСЃСЏ РѕС‚ РєРІРµСЃС‚Р°.
     */
    AbandonQuest200Response abandonQuest(String questId, AbandonQuestRequest request);

    /**
     * РџРѕР»СѓС‡РёС‚СЊ С†РµР»Рё РєРІРµСЃС‚Р°.
     */
    GetQuestObjectives200Response getQuestObjectives(String questId, UUID characterId);
}

