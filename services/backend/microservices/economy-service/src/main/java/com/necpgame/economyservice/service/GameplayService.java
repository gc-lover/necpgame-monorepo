package com.necpgame.economyservice.service;

import com.necpgame.economyservice.model.BuyItemRequest;
import com.necpgame.economyservice.model.CheckPriceRequest;
import com.necpgame.economyservice.model.Error;
import com.necpgame.economyservice.model.GetMarkets200Response;
import com.necpgame.economyservice.model.GetVendorInventory200Response;
import org.springframework.lang.Nullable;
import com.necpgame.economyservice.model.PriceCalculation;
import com.necpgame.economyservice.model.SellItemRequest;
import com.necpgame.economyservice.model.TradeResult;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for GameplayService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface GameplayService {

    /**
     * POST /gameplay/economy/trading/buy : Купить предмет
     * Покупает предмет у торговца или на рынке. Применяет модификаторы цен (репутация, навыки, события). 
     *
     * @param buyItemRequest  (required)
     * @return TradeResult
     */
    TradeResult buyItem(BuyItemRequest buyItemRequest);

    /**
     * POST /gameplay/economy/trading/price-check : Проверить цену
     * Рассчитывает цену предмета с учетом всех модификаторов. Не выполняет сделку, только показывает итоговую цену. 
     *
     * @param checkPriceRequest  (required)
     * @return PriceCalculation
     */
    PriceCalculation checkPrice(CheckPriceRequest checkPriceRequest);

    /**
     * GET /gameplay/economy/trading/markets : Получить доступные рынки
     * Возвращает список доступных рынков для торговли. Включает NPC vendors, Auction House, Black Market. 
     *
     * @param characterId  (required)
     * @param regionId  (optional)
     * @param marketType  (optional)
     * @return GetMarkets200Response
     */
    GetMarkets200Response getMarkets(String characterId, String regionId, String marketType);

    /**
     * GET /gameplay/economy/trading/vendor/{vendor_id}/inventory : Получить инвентарь торговца
     * Возвращает товары, доступные у NPC торговца. Ассортимент зависит от репутации, времени, региона. 
     *
     * @param vendorId  (required)
     * @param characterId  (required)
     * @return GetVendorInventory200Response
     */
    GetVendorInventory200Response getVendorInventory(String vendorId, String characterId);

    /**
     * POST /gameplay/economy/trading/sell : Продать предмет
     * Продает предмет торговцу или на рынок. Цена зависит от репутации, навыков, состояния предмета. 
     *
     * @param sellItemRequest  (required)
     * @return TradeResult
     */
    TradeResult sellItem(SellItemRequest sellItemRequest);
}

