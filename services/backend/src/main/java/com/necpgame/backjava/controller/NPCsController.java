package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.NpcsApi;
import com.necpgame.backjava.model.*;
import com.necpgame.backjava.service.NPCsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST Controller РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ NPC Рё РґРёР°Р»РѕРіР°РјРё.
 * 
 * Р РµР°Р»РёР·СѓРµС‚ РєРѕРЅС‚СЂР°РєС‚ {@link NpcsApi}, СЃРіРµРЅРµСЂРёСЂРѕРІР°РЅРЅС‹Р№ РёР· OpenAPI СЃРїРµС†РёС„РёРєР°С†РёРё.
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/npcs/npcs.yaml
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class NPCsController implements NpcsApi {
    
    private final NPCsService service;
    
    @Override
    public ResponseEntity<GetNPCs200Response> getNPCs(UUID characterId, String type) {
        log.info("GET /npcs?characterId={}&type={}", characterId, type);
        return ResponseEntity.ok(service.getNPCs(characterId, type));
    }
    
    @Override
    public ResponseEntity<GetNPCs200Response> getNPCsByLocation(String locationId, UUID characterId) {
        log.info("GET /npcs/location/{}?characterId={}", locationId, characterId);
        return ResponseEntity.ok(service.getNPCsByLocation(locationId, characterId));
    }
    
    @Override
    public ResponseEntity<NPC> getNPCDetails(String npcId, UUID characterId) {
        log.info("GET /npcs/{}?characterId={}", npcId, characterId);
        return ResponseEntity.ok(service.getNPCDetails(npcId, characterId));
    }
    
    @Override
    public ResponseEntity<NPCDialogue> getNPCDialogue(String npcId, UUID characterId) {
        log.info("GET /npcs/{}/dialogue?characterId={}", npcId, characterId);
        return ResponseEntity.ok(service.getNPCDialogue(npcId, characterId));
    }
    
    @Override
    public ResponseEntity<InteractWithNPC200Response> interactWithNPC(String npcId, InteractWithNPCRequest interactWithNPCRequest) {
        log.info("POST /npcs/{}/interact", npcId);
        return ResponseEntity.ok(service.interactWithNPC(npcId, interactWithNPCRequest));
    }
    
    @Override
    public ResponseEntity<NPCDialogue> respondToDialogue(String npcId, RespondToDialogueRequest respondToDialogueRequest) {
        log.info("POST /npcs/{}/dialogue/respond", npcId);
        return ResponseEntity.ok(service.respondToDialogue(npcId, respondToDialogueRequest));
    }
}

