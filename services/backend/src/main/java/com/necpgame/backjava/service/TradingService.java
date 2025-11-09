package com.necpgame.backjava.service;

import com.necpgame.backjava.model.*;

import java.util.UUID;

/**
 * TradingService - СЃРµСЂРІРёСЃ РґР»СЏ СЂР°Р±РѕС‚С‹ СЃ С‚РѕСЂРіРѕРІР»РµР№.
 * 
 * РЎРіРµРЅРµСЂРёСЂРѕРІР°РЅРѕ РЅР° РѕСЃРЅРѕРІРµ: API-SWAGGER/api/v1/trading/trading.yaml
 */
public interface TradingService {

    /**
     * РџРѕР»СѓС‡РёС‚СЊ СЃРїРёСЃРѕРє С‚РѕСЂРіРѕРІС†РµРІ.
     */
    GetVendors200Response getVendors(UUID characterId, String locationId);

    /**
     * РџРѕР»СѓС‡РёС‚СЊ РёРЅРІРµРЅС‚Р°СЂСЊ С‚РѕСЂРіРѕРІС†Р°.
     */
    VendorInventory getVendorInventory(String vendorId, UUID characterId);

    /**
     * РљСѓРїРёС‚СЊ РїСЂРµРґРјРµС‚ Сѓ С‚РѕСЂРіРѕРІС†Р°.
     */
    BuyItem200Response buyItem(BuyItemRequest request);

    /**
     * РџСЂРѕРґР°С‚СЊ РїСЂРµРґРјРµС‚ С‚РѕСЂРіРѕРІС†Сѓ.
     */
    SellItem200Response sellItem(BuyItemRequest request);

    /**
     * РџРѕР»СѓС‡РёС‚СЊ С†РµРЅСѓ РїСЂРµРґРјРµС‚Р°.
     */
    GetItemPrice200Response getItemPrice(String itemId, String vendorId, UUID characterId);
}

