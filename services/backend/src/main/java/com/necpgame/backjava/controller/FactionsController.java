package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.FactionsApi;
import com.necpgame.backjava.model.GetFactions200Response;
import com.necpgame.backjava.service.FactionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ С„СЂР°РєС†РёСЏРјРё
 * Р РµР°Р»РёР·СѓРµС‚ СЃРіРµРЅРµСЂРёСЂРѕРІР°РЅРЅС‹Р№ FactionsApi РёРЅС‚РµСЂС„РµР№СЃ РёР· OpenAPI СЃРїРµС†РёС„РёРєР°С†РёРё
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class FactionsController implements FactionsApi {
    
    private final FactionsService factionsService;
    
    /**
     * GET /factions - РЎРїРёСЃРѕРє РґРѕСЃС‚СѓРїРЅС‹С… С„СЂР°РєС†РёР№
     * OpenAPI СЃРїРµС†РёС„РёРєР°С†РёСЏ РѕРїСЂРµРґРµР»СЏРµС‚ РІСЃРµ Р°РЅРЅРѕС‚Р°С†РёРё (@RequestMapping, @RequestParam)
     */
    @Override
    public ResponseEntity<GetFactions200Response> getFactions(String origin) {
        log.info("GET /factions?origin={}", origin);
        GetFactions200Response response = factionsService.getFactions(origin);
        return ResponseEntity.ok(response);
    }

}

