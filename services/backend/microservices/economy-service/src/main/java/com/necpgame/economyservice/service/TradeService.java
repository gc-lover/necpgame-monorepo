package com.necpgame.economyservice.service;

import com.necpgame.economyservice.model.ConfirmTrade200Response;
import com.necpgame.economyservice.model.ConfirmTradeRequest;
import com.necpgame.economyservice.model.InitiateTrade200Response;
import com.necpgame.economyservice.model.InitiateTradeRequest;
import com.necpgame.economyservice.model.MakeTradeOfferRequest;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for TradeService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface TradeService {

    /**
     * POST /trade/{session_id}/accept : Принять торговлю
     * Принимает запрос на торговлю, открывает trade window
     *
     * @param sessionId  (required)
     * @return Object
     */
    Object acceptTrade(String sessionId);

    /**
     * POST /trade/{session_id}/cancel : Отменить торговлю
     * Отменяет активную торговлю
     *
     * @param sessionId  (required)
     * @return Object
     */
    Object cancelTrade(String sessionId);

    /**
     * POST /trade/{session_id}/confirm : Подтвердить торговлю
     * Подтверждает торговлю. Требует подтверждение обеих сторон для завершения. 
     *
     * @param sessionId  (required)
     * @param confirmTradeRequest  (required)
     * @return ConfirmTrade200Response
     */
    ConfirmTrade200Response confirmTrade(String sessionId, ConfirmTradeRequest confirmTradeRequest);

    /**
     * POST /trade/initiate : Начать торговлю
     * Отправляет запрос на торговлю другому игроку. Требует distance check (10m). 
     *
     * @param initiateTradeRequest  (required)
     * @return InitiateTrade200Response
     */
    InitiateTrade200Response initiateTrade(InitiateTradeRequest initiateTradeRequest);

    /**
     * POST /trade/{session_id}/offer : Сделать предложение
     * Добавляет items/gold в trade window. Можно изменять до подтверждения. 
     *
     * @param sessionId  (required)
     * @param makeTradeOfferRequest  (required)
     * @return Object
     */
    Object makeTradeOffer(String sessionId, MakeTradeOfferRequest makeTradeOfferRequest);
}

