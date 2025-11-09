package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.QuestsApi;
import com.necpgame.backjava.model.*;
import com.necpgame.backjava.service.QuestsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST Controller РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ РєРІРµСЃС‚Р°РјРё.
 * 
 * Р РµР°Р»РёР·СѓРµС‚ РєРѕРЅС‚СЂР°РєС‚ {@link QuestsQuestsApi}, СЃРіРµРЅРµСЂРёСЂРѕРІР°РЅРЅС‹Р№ РёР· OpenAPI.
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/quests/quests.yaml
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class QuestsController implements QuestsApi {
    
    private final QuestsService service;
    
    @Override
    public ResponseEntity<GetAvailableQuests200Response> getAvailableQuests(UUID characterId, String type) {
        log.info("GET /quests?characterId={}&type={}", characterId, type);
        return ResponseEntity.ok(service.getAvailableQuests(characterId, type));
    }
    
    @Override
    public ResponseEntity<GetActiveQuests200Response> getActiveQuests(UUID characterId) {
        log.info("GET /quests/active?characterId={}", characterId);
        return ResponseEntity.ok(service.getActiveQuests(characterId));
    }
    
    @Override
    public ResponseEntity<Quest> getQuestDetails(String questId, UUID characterId) {
        log.info("GET /quests/{}?characterId={}", questId, characterId);
        return ResponseEntity.ok(service.getQuestDetails(questId, characterId));
    }
    
    // TODO: Р’СЂРµРјРµРЅРЅРѕ Р·Р°РєРѕРјРјРµРЅС‚РёСЂРѕРІР°РЅРѕ - РЅРµСЃРѕРѕС‚РІРµС‚СЃС‚РІРёРµ API РёРЅС‚РµСЂС„РµР№СЃСѓ
    // @Override
    // public ResponseEntity<AcceptQuest200Response> acceptQuest(String questId, AcceptQuestRequest acceptQuestRequest) {
    //     log.info("POST /quests/{}/accept", questId);
    //     return ResponseEntity.ok(service.acceptQuest(questId, acceptQuestRequest));
    // }
    
    // @Override
    // public ResponseEntity<CompleteQuest200Response> completeQuest(String questId, CompleteQuestRequest completeQuestRequest) {
    //     log.info("POST /quests/{}/complete", questId);
    //     return ResponseEntity.ok(service.completeQuest(questId, completeQuestRequest));
    // }
    
    // @Override
    // public ResponseEntity<AbandonQuest200Response> abandonQuest(String questId, AbandonQuestRequest abandonQuestRequest) {
    //     log.info("POST /quests/{}/abandon", questId);
    //     return ResponseEntity.ok(service.abandonQuest(questId, abandonQuestRequest));
    // }
    
    @Override
    public ResponseEntity<GetQuestObjectives200Response> getQuestObjectives(String questId, UUID characterId) {
        log.info("GET /quests/{}/objectives?characterId={}", questId, characterId);
        return ResponseEntity.ok(service.getQuestObjectives(questId, characterId));
    }
}

