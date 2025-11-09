package com.necpgame.backjava.service;

import com.necpgame.backjava.model.ConvertRequest;
import com.necpgame.backjava.model.ConvertResult;
import com.necpgame.backjava.model.CurrencyPairRate;
import com.necpgame.backjava.model.ExchangeOrder;
import com.necpgame.backjava.model.ExchangeOrderRequest;
import com.necpgame.backjava.model.ExchangeRates;
import com.necpgame.backjava.model.FindArbitrageOpportunities200Response;
import com.necpgame.backjava.model.GetAvailablePairs200Response;
import com.necpgame.backjava.model.RateHistory;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for CurrencyExchangeService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface CurrencyExchangeService {

    /**
     * DELETE /gameplay/economy/currency-exchange/trading/orders/{order_id} : Отменить ордер
     *
     * @param orderId  (required)
     * @return Void
     */
    Void cancelExchangeOrder(UUID orderId);

    /**
     * POST /gameplay/economy/currency-exchange/convert : Конвертировать валюту
     *
     * @param convertRequest  (required)
     * @return ConvertResult
     */
    ConvertResult convertCurrency(ConvertRequest convertRequest);

    /**
     * GET /gameplay/economy/currency-exchange/arbitrage : Найти арбитражные возможности
     * Региональный и triangular arbitrage
     *
     * @return FindArbitrageOpportunities200Response
     */
    FindArbitrageOpportunities200Response findArbitrageOpportunities();

    /**
     * GET /gameplay/economy/currency-exchange/pairs : Получить доступные валютные пары
     *
     * @param type Тип пары (optional)
     * @return GetAvailablePairs200Response
     */
    GetAvailablePairs200Response getAvailablePairs(String type);

    /**
     * GET /gameplay/economy/currency-exchange/trading/orders/{order_id} : Получить статус ордера
     *
     * @param orderId  (required)
     * @return ExchangeOrder
     */
    ExchangeOrder getExchangeOrder(UUID orderId);

    /**
     * GET /gameplay/economy/currency-exchange/rates : Получить текущие курсы валют
     *
     * @return ExchangeRates
     */
    ExchangeRates getExchangeRates();

    /**
     * GET /gameplay/economy/currency-exchange/rates/{pair} : Получить курс для конкретной пары
     *
     * @param pair Валютная пара (например NCRD/EURO) (required)
     * @return CurrencyPairRate
     */
    CurrencyPairRate getPairRate(String pair);

    /**
     * GET /gameplay/economy/currency-exchange/rates/history : Получить историю курсов
     *
     * @param pair  (required)
     * @param period Период (1h, 24h, 7d, 30d, 1y) (optional, default to 24h)
     * @param interval Интервал данных (1m, 5m, 1h, 1d) (optional, default to 1h)
     * @return RateHistory
     */
    RateHistory getRateHistory(String pair, String period, String interval);

    /**
     * POST /gameplay/economy/currency-exchange/trading/orders : Разместить торговый ордер
     * Leverage trading до 10x
     *
     * @param exchangeOrderRequest  (required)
     * @return ExchangeOrder
     */
    ExchangeOrder placeExchangeOrder(ExchangeOrderRequest exchangeOrderRequest);
}

