package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.TradeApi;
import com.necpgame.backjava.model.*;
import com.necpgame.backjava.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST Controller для системы торговли
 * Implements TradeApi - сгенерирован из trade-system.yaml
 */
@RestController
@RequiredArgsConstructor
public class TradeController implements TradeApi {

    private final TradeService tradeService;

    @Override
    public ResponseEntity<Object> acceptTrade(String sessionId) {
        return ResponseEntity.ok(tradeService.acceptTrade(sessionId));
    }

    @Override
    public ResponseEntity<Object> cancelTrade(String sessionId) {
        return ResponseEntity.ok(tradeService.cancelTrade(sessionId));
    }

    @Override
    public ResponseEntity<ConfirmTrade200Response> confirmTrade(String sessionId, ConfirmTradeRequest confirmTradeRequest) {
        return ResponseEntity.ok(tradeService.confirmTrade(sessionId, confirmTradeRequest));
    }

    @Override
    public ResponseEntity<InitiateTrade200Response> initiateTrade(InitiateTradeRequest initiateTradeRequest) {
        return ResponseEntity.ok(tradeService.initiateTrade(initiateTradeRequest));
    }

    @Override
    public ResponseEntity<Object> makeTradeOffer(String sessionId, MakeTradeOfferRequest makeTradeOfferRequest) {
        return ResponseEntity.ok(tradeService.makeTradeOffer(sessionId, makeTradeOfferRequest));
    }
}

