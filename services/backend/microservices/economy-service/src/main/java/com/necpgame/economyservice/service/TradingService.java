package com.necpgame.economyservice.service;

import com.necpgame.economyservice.model.BuyItem200Response;
import com.necpgame.economyservice.model.BuyItemRequest;
import com.necpgame.economyservice.model.GetItemPrice200Response;
import com.necpgame.economyservice.model.GetVendors200Response;
import org.springframework.lang.Nullable;
import com.necpgame.economyservice.model.SellItem200Response;
import java.util.UUID;
import com.necpgame.economyservice.model.VendorInventory;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for TradingService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface TradingService {

    /**
     * POST /trading/buy : Купить предмет
     *
     * @param buyItemRequest  (optional)
     * @return BuyItem200Response
     */
    BuyItem200Response buyItem(BuyItemRequest buyItemRequest);

    /**
     * GET /trading/prices/{itemId} : Цена предмета у торговца
     *
     * @param itemId  (required)
     * @param vendorId  (required)
     * @param characterId  (required)
     * @return GetItemPrice200Response
     */
    GetItemPrice200Response getItemPrice(String itemId, String vendorId, UUID characterId);

    /**
     * GET /trading/vendors/{vendorId}/inventory : Ассортимент торговца
     *
     * @param vendorId  (required)
     * @param characterId  (required)
     * @return VendorInventory
     */
    VendorInventory getVendorInventory(String vendorId, UUID characterId);

    /**
     * GET /trading/vendors : Список торговцев
     *
     * @param characterId  (required)
     * @param locationId  (optional)
     * @return GetVendors200Response
     */
    GetVendors200Response getVendors(UUID characterId, String locationId);

    /**
     * POST /trading/sell : Продать предмет
     *
     * @param buyItemRequest  (optional)
     * @return SellItem200Response
     */
    SellItem200Response sellItem(BuyItemRequest buyItemRequest);
}

