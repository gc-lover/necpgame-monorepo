package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.TradingApi;
import com.necpgame.backjava.model.*;
import com.necpgame.backjava.service.TradingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * REST Controller РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ С‚РѕСЂРіРѕРІР»РµР№.
 * 
 * Р РµР°Р»РёР·СѓРµС‚ РєРѕРЅС‚СЂР°РєС‚ {@link TradingApi}, СЃРіРµРЅРµСЂРёСЂРѕРІР°РЅРЅС‹Р№ РёР· OpenAPI СЃРїРµС†РёС„РёРєР°С†РёРё.
 * РСЃС‚РѕС‡РЅРёРє: API-SWAGGER/api/v1/trading/trading.yaml
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class TradingController implements TradingApi {
    
    private final TradingService service;
    
    @Override
    public ResponseEntity<GetVendors200Response> getVendors(UUID characterId, String locationId) {
        log.info("GET /vendors?characterId={}&locationId={}", characterId, locationId);
        return ResponseEntity.ok(service.getVendors(characterId, locationId));
    }
    
    @Override
    public ResponseEntity<VendorInventory> getVendorInventory(String vendorId, UUID characterId) {
        log.info("GET /vendors/{}/inventory?characterId={}", vendorId, characterId);
        return ResponseEntity.ok(service.getVendorInventory(vendorId, characterId));
    }
    
    @Override
    public ResponseEntity<BuyItem200Response> buyItem(BuyItemRequest buyItemRequest) {
        log.info("POST /trading/buy");
        return ResponseEntity.ok(service.buyItem(buyItemRequest));
    }
    
    @Override
    public ResponseEntity<SellItem200Response> sellItem(BuyItemRequest buyItemRequest) {
        log.info("POST /trading/sell");
        return ResponseEntity.ok(service.sellItem(buyItemRequest));
    }
    
    @Override
    public ResponseEntity<GetItemPrice200Response> getItemPrice(String itemId, String vendorId, UUID characterId) {
        log.info("GET /trading/price?itemId={}&vendorId={}&characterId={}", itemId, vendorId, characterId);
        return ResponseEntity.ok(service.getItemPrice(itemId, vendorId, characterId));
    }
}

