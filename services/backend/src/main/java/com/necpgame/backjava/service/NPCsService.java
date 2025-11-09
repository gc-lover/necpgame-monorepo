package com.necpgame.backjava.service;

import com.necpgame.backjava.model.*;

import java.util.UUID;

/**
 * NPCsService - СЃРµСЂРІРёСЃ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ NPC Рё РґРёР°Р»РѕРіР°РјРё.
 * 
 * РЎРіРµРЅРµСЂРёСЂРѕРІР°РЅРѕ РёР·: API-SWAGGER/api/v1/npcs/npcs.yaml
 * 
 * РќР• СЂРµРґР°РєС‚РёСЂСѓР№С‚Рµ СЌС‚РѕС‚ С„Р°Р№Р» РІСЂСѓС‡РЅСѓСЋ - РѕРЅ РіРµРЅРµСЂРёСЂСѓРµС‚СЃСЏ Р°РІС‚РѕРјР°С‚РёС‡РµСЃРєРё!
 */
public interface NPCsService {

    /**
     * РџРѕР»СѓС‡РёС‚СЊ СЃРїРёСЃРѕРє РІСЃРµС… NPC.
     */
    GetNPCs200Response getNPCs(UUID characterId, String type);

    /**
     * РџРѕР»СѓС‡РёС‚СЊ NPC РІ Р»РѕРєР°С†РёРё.
     */
    GetNPCs200Response getNPCsByLocation(String locationId, UUID characterId);

    /**
     * РџРѕР»СѓС‡РёС‚СЊ РґРµС‚Р°Р»Рё NPC.
     */
    NPC getNPCDetails(String npcId, UUID characterId);

    /**
     * РџРѕР»СѓС‡РёС‚СЊ РґРёР°Р»РѕРі СЃ NPC.
     */
    NPCDialogue getNPCDialogue(String npcId, UUID characterId);

    /**
     * Р’Р·Р°РёРјРѕРґРµР№СЃС‚РІРѕРІР°С‚СЊ СЃ NPC.
     */
    InteractWithNPC200Response interactWithNPC(String npcId, InteractWithNPCRequest request);

    /**
     * РћС‚РІРµС‚РёС‚СЊ РІ РґРёР°Р»РѕРіРµ СЃ NPC.
     */
    NPCDialogue respondToDialogue(String npcId, RespondToDialogueRequest request);
}

