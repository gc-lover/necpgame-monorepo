package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.CurrencyExchangeApi;
import com.necpgame.backjava.model.ConvertRequest;
import com.necpgame.backjava.model.ConvertResult;
import com.necpgame.backjava.model.CurrencyPairRate;
import com.necpgame.backjava.model.ExchangeOrder;
import com.necpgame.backjava.model.ExchangeOrderRequest;
import com.necpgame.backjava.model.ExchangeRates;
import com.necpgame.backjava.model.FindArbitrageOpportunities200Response;
import com.necpgame.backjava.model.GetAvailablePairs200Response;
import com.necpgame.backjava.model.RateHistory;
import com.necpgame.backjava.service.CurrencyExchangeService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class CurrencyExchangeController implements CurrencyExchangeApi {

    private final CurrencyExchangeService currencyExchangeService;

    @Override
    public ResponseEntity<Void> cancelExchangeOrder(UUID orderId) {
        log.info("DELETE /gameplay/economy/currency-exchange/trading/orders/{}", orderId);
        currencyExchangeService.cancelExchangeOrder(orderId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<ConvertResult> convertCurrency(ConvertRequest convertRequest) {
        log.info("POST /gameplay/economy/currency-exchange/convert [characterId={}]", convertRequest.getCharacterId());
        return ResponseEntity.ok(currencyExchangeService.convertCurrency(convertRequest));
    }

    @Override
    public ResponseEntity<FindArbitrageOpportunities200Response> findArbitrageOpportunities() {
        log.info("GET /gameplay/economy/currency-exchange/arbitrage");
        return ResponseEntity.ok(currencyExchangeService.findArbitrageOpportunities());
    }

    @Override
    public ResponseEntity<GetAvailablePairs200Response> getAvailablePairs(@Nullable String type) {
        log.info("GET /gameplay/economy/currency-exchange/pairs [type={}]", type);
        return ResponseEntity.ok(currencyExchangeService.getAvailablePairs(type));
    }

    @Override
    public ResponseEntity<ExchangeOrder> getExchangeOrder(UUID orderId) {
        log.info("GET /gameplay/economy/currency-exchange/trading/orders/{}", orderId);
        return ResponseEntity.ok(currencyExchangeService.getExchangeOrder(orderId));
    }

    @Override
    public ResponseEntity<ExchangeRates> getExchangeRates() {
        log.info("GET /gameplay/economy/currency-exchange/rates");
        return ResponseEntity.ok(currencyExchangeService.getExchangeRates());
    }

    @Override
    public ResponseEntity<CurrencyPairRate> getPairRate(String pair) {
        log.info("GET /gameplay/economy/currency-exchange/rates/{}", pair);
        return ResponseEntity.ok(currencyExchangeService.getPairRate(pair));
    }

    @Override
    public ResponseEntity<RateHistory> getRateHistory(String pair, @Nullable String period, @Nullable String interval) {
        log.info("GET /gameplay/economy/currency-exchange/rates/history [pair={}, period={}, interval={}]", pair, period, interval);
        return ResponseEntity.ok(currencyExchangeService.getRateHistory(pair, period, interval));
    }

    @Override
    public ResponseEntity<ExchangeOrder> placeExchangeOrder(ExchangeOrderRequest exchangeOrderRequest) {
        log.info("POST /gameplay/economy/currency-exchange/trading/orders [characterId={}, pair={}]", exchangeOrderRequest.getCharacterId(), exchangeOrderRequest.getPair());
        ExchangeOrder response = currencyExchangeService.placeExchangeOrder(exchangeOrderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}


