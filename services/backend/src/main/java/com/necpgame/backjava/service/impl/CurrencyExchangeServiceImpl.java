package com.necpgame.backjava.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.CurrencyArbitrageOpportunityEntity;
import com.necpgame.backjava.entity.CurrencyExchangeOrderEntity;
import com.necpgame.backjava.entity.CurrencyExchangeRateEntity;
import com.necpgame.backjava.entity.CurrencyPairEntity;
import com.necpgame.backjava.entity.CurrencyPairRateEntity;
import com.necpgame.backjava.entity.CurrencyRateHistoryEntity;
import com.necpgame.backjava.model.ArbitrageOpportunity;
import com.necpgame.backjava.model.ConvertRequest;
import com.necpgame.backjava.model.ConvertResult;
import com.necpgame.backjava.model.CurrencyPair;
import com.necpgame.backjava.model.CurrencyPairRate;
import com.necpgame.backjava.model.ExchangeOrder;
import com.necpgame.backjava.model.ExchangeOrderRequest;
import com.necpgame.backjava.model.ExchangeRates;
import com.necpgame.backjava.model.FindArbitrageOpportunities200Response;
import com.necpgame.backjava.model.GetAvailablePairs200Response;
import com.necpgame.backjava.model.RateHistory;
import com.necpgame.backjava.model.RateHistoryDataInner;
import com.necpgame.backjava.repository.CurrencyArbitrageOpportunityRepository;
import com.necpgame.backjava.repository.CurrencyExchangeOrderRepository;
import com.necpgame.backjava.repository.CurrencyExchangeRateRepository;
import com.necpgame.backjava.repository.CurrencyPairRateRepository;
import com.necpgame.backjava.repository.CurrencyPairRepository;
import com.necpgame.backjava.repository.CurrencyRateHistoryRepository;
import com.necpgame.backjava.service.CurrencyExchangeService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {

    private static final double DEFAULT_COMMISSION_RATE = 0.0025d;
    private static final String DEFAULT_PERIOD = "24h";
    private static final String DEFAULT_INTERVAL = "1h";

    private final CurrencyExchangeRateRepository currencyExchangeRateRepository;
    private final CurrencyPairRepository currencyPairRepository;
    private final CurrencyPairRateRepository currencyPairRateRepository;
    private final CurrencyRateHistoryRepository currencyRateHistoryRepository;
    private final CurrencyExchangeOrderRepository currencyExchangeOrderRepository;
    private final CurrencyArbitrageOpportunityRepository currencyArbitrageOpportunityRepository;
    private final ObjectMapper objectMapper;

    @Override
    public ExchangeRates getExchangeRates() {
        CurrencyExchangeRateEntity entity = currencyExchangeRateRepository.findFirstByOrderByTimestampDesc()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Exchange rates not available"));

        Map<String, Float> rates = toFloatMap(readRates(entity.getRatesJson()));

        return new ExchangeRates()
            .baseCurrency(entity.getBaseCurrency())
            .timestamp(toOffset(entity.getTimestamp()))
            .rates(rates);
    }

    @Override
    public CurrencyPairRate getPairRate(String pair) {
        String normalizedPair = normalizePair(pair);
        CurrencyPairRateEntity entity = currencyPairRateRepository.findFirstByPairOrderByTimestampDesc(normalizedPair)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pair rate not found"));
        return mapPairRate(entity);
    }

    @Override
    public RateHistory getRateHistory(String pair, String period, String interval) {
        String normalizedPair = normalizePair(pair);
        String resolvedPeriod = StringUtils.hasText(period) ? period : DEFAULT_PERIOD;
        String resolvedInterval = StringUtils.hasText(interval) ? interval : DEFAULT_INTERVAL;

        List<CurrencyRateHistoryEntity> entities = currencyRateHistoryRepository
            .findTop500ByPairAndPeriodIgnoreCaseAndIntervalIgnoreCaseOrderByTimestampDesc(normalizedPair, resolvedPeriod, resolvedInterval);

        if (entities.isEmpty()) {
            entities = currencyRateHistoryRepository.findTop500ByPairOrderByTimestampDesc(normalizedPair);
        }

        if (entities.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rate history not available");
        }

        List<RateHistoryDataInner> data = entities.stream()
            .sorted(Comparator.comparing(CurrencyRateHistoryEntity::getTimestamp))
            .map(this::mapRateHistoryPoint)
            .toList();

        return new RateHistory()
            .pair(normalizedPair)
            .period(resolvedPeriod)
            .interval(resolvedInterval)
            .data(new ArrayList<>(data));
    }

    @Override
    @Transactional
    public ConvertResult convertCurrency(ConvertRequest convertRequest) {
        if (convertRequest == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required");
        }
        if (convertRequest.getAmount() == null || convertRequest.getAmount() <= 0f) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive");
        }

        String fromCurrency = normalizeCurrency(convertRequest.getFromCurrency());
        String toCurrency = normalizeCurrency(convertRequest.getToCurrency());

        ConversionContext context = resolveConversionContext(fromCurrency, toCurrency);

        double amountFrom = convertRequest.getAmount();
        double amountTo = amountFrom * context.effectiveRate();

        CurrencyPairEntity pairEntity = currencyPairRepository.findByPair(context.targetPair()).orElse(null);
        double commissionRate = pairEntity != null && pairEntity.getCommissionRate() != null
            ? pairEntity.getCommissionRate()
            : DEFAULT_COMMISSION_RATE;

        double commission = amountTo * commissionRate;
        double totalCost = amountTo + commission;

        return new ConvertResult()
            .fromCurrency(fromCurrency)
            .toCurrency(toCurrency)
            .amountFrom((float) amountFrom)
            .amountTo((float) amountTo)
            .exchangeRate((float) context.effectiveRate())
            .commission((float) commission)
            .totalCost((float) totalCost)
            .timestamp(toOffset(context.rateEntity().getTimestamp()));
    }

    @Override
    public GetAvailablePairs200Response getAvailablePairs(String type) {
        List<CurrencyPairEntity> pairs;
        if (StringUtils.hasText(type)) {
            CurrencyPairEntity.PairType pairType = parsePairType(type);
            pairs = currencyPairRepository.findAllByPairTypeOrderByPairAsc(pairType);
        } else {
            pairs = currencyPairRepository.findAllByOrderByPairAsc();
        }

        List<CurrencyPair> dtoPairs = pairs.stream()
            .map(this::mapCurrencyPair)
            .toList();

        return new GetAvailablePairs200Response().pairs(new ArrayList<>(dtoPairs));
    }

    @Override
    @Transactional
    public ExchangeOrder placeExchangeOrder(ExchangeOrderRequest exchangeOrderRequest) {
        if (exchangeOrderRequest == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request body is required");
        }
        if (exchangeOrderRequest.getAmount() == null || exchangeOrderRequest.getAmount() <= 0f) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be positive");
        }
        UUID characterId = exchangeOrderRequest.getCharacterId();
        if (characterId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Character id is required");
        }
        if (!StringUtils.hasText(exchangeOrderRequest.getPair())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pair is required");
        }
        if (exchangeOrderRequest.getLeverage() != null && (exchangeOrderRequest.getLeverage() < 1 || exchangeOrderRequest.getLeverage() > 10)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leverage must be between 1 and 10");
        }
        CurrencyExchangeOrderEntity.OrderType orderType = resolveOrderType(exchangeOrderRequest.getOrderType());
        CurrencyExchangeOrderEntity.OrderSide side = resolveSide(exchangeOrderRequest.getSide());

        Float limitPrice = extractNullable(exchangeOrderRequest.getLimitPrice());
        Float stopLoss = extractNullable(exchangeOrderRequest.getStopLoss());
        Float takeProfit = extractNullable(exchangeOrderRequest.getTakeProfit());

        boolean isLimit = orderType == CurrencyExchangeOrderEntity.OrderType.LIMIT;
        if (isLimit && limitPrice == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Limit price is required for limit orders");
        }

        CurrencyExchangeOrderEntity entity = CurrencyExchangeOrderEntity.builder()
            .characterId(characterId)
            .pair(normalizePair(exchangeOrderRequest.getPair()))
            .side(side)
            .amount(exchangeOrderRequest.getAmount().doubleValue())
            .orderType(orderType)
            .limitPrice(limitPrice != null ? limitPrice.doubleValue() : null)
            .leverage(exchangeOrderRequest.getLeverage() != null ? exchangeOrderRequest.getLeverage() : 1)
            .stopLoss(stopLoss != null ? stopLoss.doubleValue() : null)
            .takeProfit(takeProfit != null ? takeProfit.doubleValue() : null)
            .status(CurrencyExchangeOrderEntity.OrderStatus.PENDING)
            .filledAmount(0d)
            .averagePrice(0d)
            .build();

        Objects.requireNonNull(entity, "Order entity must not be null");
        CurrencyExchangeOrderEntity saved = currencyExchangeOrderRepository.save(entity);
        return mapOrder(saved);
    }

    @Override
    public ExchangeOrder getExchangeOrder(UUID orderId) {
        UUID requiredOrderId = Objects.requireNonNull(orderId, "orderId must not be null");
        CurrencyExchangeOrderEntity entity = currencyExchangeOrderRepository.findById(requiredOrderId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        return mapOrder(entity);
    }

    @Override
    @Transactional
    public Void cancelExchangeOrder(UUID orderId) {
        UUID requiredOrderId = Objects.requireNonNull(orderId, "orderId must not be null");
        CurrencyExchangeOrderEntity entity = currencyExchangeOrderRepository.findById(requiredOrderId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));

        if (entity.getStatus() == CurrencyExchangeOrderEntity.OrderStatus.FILLED
            || entity.getStatus() == CurrencyExchangeOrderEntity.OrderStatus.CANCELLED
            || entity.getStatus() == CurrencyExchangeOrderEntity.OrderStatus.EXPIRED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Order cannot be cancelled");
        }

        entity.setStatus(CurrencyExchangeOrderEntity.OrderStatus.CANCELLED);
        entity.setFilledAt(LocalDateTime.now(ZoneOffset.UTC));
        currencyExchangeOrderRepository.save(entity);
        return null;
    }

    @Override
    public FindArbitrageOpportunities200Response findArbitrageOpportunities() {
        List<CurrencyArbitrageOpportunityEntity> entities = currencyArbitrageOpportunityRepository.findAllByOrderByProfitPotentialDesc();
        List<ArbitrageOpportunity> dtos = entities.stream()
            .filter(this::isOpportunityActive)
            .map(this::mapArbitrageOpportunity)
            .toList();

        return new FindArbitrageOpportunities200Response().opportunities(new ArrayList<>(dtos));
    }

    private ConversionContext resolveConversionContext(String fromCurrency, String toCurrency) {
        String directPair = fromCurrency + "/" + toCurrency;
        Optional<CurrencyPairRateEntity> direct = currencyPairRateRepository.findFirstByPairOrderByTimestampDesc(directPair);
        if (direct.isPresent()) {
            return new ConversionContext(directPair, direct.get(), effectiveRate(direct.get(), false));
        }

        String inversePair = toCurrency + "/" + fromCurrency;
        Optional<CurrencyPairRateEntity> inverse = currencyPairRateRepository.findFirstByPairOrderByTimestampDesc(inversePair);
        if (inverse.isPresent()) {
            double rate = effectiveRate(inverse.get(), true);
            return new ConversionContext(inversePair, inverse.get(), rate);
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pair rate not found");
    }

    private double effectiveRate(CurrencyPairRateEntity entity, boolean inverted) {
        Double baseRate = entity.getRate();
        if (baseRate == null) {
            baseRate = entity.getAsk() != null ? entity.getAsk() : entity.getBid();
        }
        if (baseRate == null || baseRate <= 0d) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid rate data");
        }
        return inverted ? 1d / baseRate : baseRate;
    }

    private CurrencyPairRate mapPairRate(CurrencyPairRateEntity entity) {
        return new CurrencyPairRate()
            .pair(entity.getPair())
            .base(entity.getBaseCurrency())
            .quote(entity.getQuoteCurrency())
            .rate(toFloat(entity.getRate()))
            .bid(toFloat(entity.getBid()))
            .ask(toFloat(entity.getAsk()))
            .spread(toFloat(entity.getSpread()))
            .timestamp(toOffset(entity.getTimestamp()));
    }

    private RateHistoryDataInner mapRateHistoryPoint(CurrencyRateHistoryEntity entity) {
        return new RateHistoryDataInner()
            .timestamp(toOffset(entity.getTimestamp()))
            .open(toFloat(entity.getOpenRate()))
            .high(toFloat(entity.getHighRate()))
            .low(toFloat(entity.getLowRate()))
            .close(toFloat(entity.getCloseRate()))
            .volume(entity.getVolume() != null ? entity.getVolume().intValue() : null);
    }

    private CurrencyPair mapCurrencyPair(CurrencyPairEntity entity) {
        CurrencyPair result = new CurrencyPair()
            .pair(entity.getPair())
            .base(entity.getBase())
            .quote(entity.getQuote())
            .minTradeAmount(entity.getMinTradeAmount())
            .maxLeverage(entity.getMaxLeverage())
            .commissionRate(toFloat(entity.getCommissionRate()));

        if (entity.getPairType() != null) {
            result.setType(CurrencyPair.TypeEnum.valueOf(entity.getPairType().name()));
        }
        return result;
    }

    private ExchangeOrder mapOrder(CurrencyExchangeOrderEntity entity) {
        ExchangeOrder exchangeOrder = new ExchangeOrder()
            .orderId(entity.getOrderId())
            .characterId(entity.getCharacterId())
            .pair(entity.getPair())
            .amount(entity.getAmount() != null ? BigDecimal.valueOf(entity.getAmount()) : null)
            .orderType(entity.getOrderType() != null ? entity.getOrderType().name() : null)
            .status(entity.getStatus() != null ? ExchangeOrder.StatusEnum.valueOf(entity.getStatus().name()) : null)
            .filledAmount(entity.getFilledAmount() != null ? BigDecimal.valueOf(entity.getFilledAmount()) : null)
            .averagePrice(entity.getAveragePrice() != null ? BigDecimal.valueOf(entity.getAveragePrice()) : null)
            .leverage(entity.getLeverage())
            .createdAt(toOffset(entity.getCreatedAt()));

        if (entity.getSide() != null) {
            exchangeOrder.setSide(ExchangeOrder.SideEnum.valueOf(entity.getSide().name()));
        }
        if (entity.getFilledAt() != null) {
            exchangeOrder.filledAt(toOffset(entity.getFilledAt()));
        }
        return exchangeOrder;
    }

    private ArbitrageOpportunity mapArbitrageOpportunity(CurrencyArbitrageOpportunityEntity entity) {
        List<String> pairs = readPairs(entity.getPairsJson());
        ArbitrageOpportunity result = new ArbitrageOpportunity()
            .pairs(pairs)
            .profitPotential(toFloat(entity.getProfitPotential()))
            .description(entity.getDescription())
            .expiresInSeconds(entity.getExpiresInSeconds());

        if (entity.getOpportunityType() != null) {
            result.setType(ArbitrageOpportunity.TypeEnum.valueOf(entity.getOpportunityType().name()));
        }
        return result;
    }

    private boolean isOpportunityActive(CurrencyArbitrageOpportunityEntity entity) {
        if (entity.getExpiresInSeconds() == null || entity.getUpdatedAt() == null) {
            return true;
        }
        LocalDateTime expiry = entity.getUpdatedAt().plusSeconds(entity.getExpiresInSeconds());
        return expiry.isAfter(LocalDateTime.now(ZoneOffset.UTC));
    }

    private CurrencyPairEntity.PairType parsePairType(String type) {
        try {
            return CurrencyPairEntity.PairType.valueOf(type.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unknown pair type: " + type);
        }
    }

    private CurrencyExchangeOrderEntity.OrderSide resolveSide(ExchangeOrderRequest.SideEnum side) {
        if (side == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Side is required");
        }
        return CurrencyExchangeOrderEntity.OrderSide.valueOf(side.name());
    }

    private CurrencyExchangeOrderEntity.OrderType resolveOrderType(ExchangeOrderRequest.OrderTypeEnum orderType) {
        if (orderType == null) {
            return CurrencyExchangeOrderEntity.OrderType.MARKET;
        }
        return CurrencyExchangeOrderEntity.OrderType.valueOf(orderType.name());
    }

    private Map<String, Double> readRates(String json) {
        if (!StringUtils.hasText(json)) {
            return Collections.emptyMap();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Double>>() {});
        } catch (Exception ex) {
            log.error("Failed to deserialize rates json", ex);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to read rates data");
        }
    }

    private List<String> readPairs(String json) {
        if (!StringUtils.hasText(json)) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<String>>() {});
        } catch (Exception ex) {
            log.error("Failed to deserialize pairs json", ex);
            return Collections.emptyList();
        }
    }

    private Map<String, Float> toFloatMap(Map<String, Double> source) {
        Map<String, Float> result = new HashMap<>();
        source.forEach((key, value) -> {
            if (value != null) {
                result.put(key, value.floatValue());
            }
        });
        return result;
    }

    private Float toFloat(Double value) {
        return value != null ? value.floatValue() : null;
    }

    private Float extractNullable(JsonNullable<Float> nullable) {
        return nullable != null && nullable.isPresent() ? nullable.get() : null;
    }

    private OffsetDateTime toOffset(LocalDateTime value) {
        return value != null ? value.atOffset(ZoneOffset.UTC) : null;
    }

    private String normalizePair(String pair) {
        if (!StringUtils.hasText(pair)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pair is required");
        }
        return pair.trim().toUpperCase();
    }

    private String normalizeCurrency(String currency) {
        if (!StringUtils.hasText(currency)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency is required");
        }
        return currency.trim().toUpperCase();
    }

    private record ConversionContext(String targetPair, CurrencyPairRateEntity rateEntity, double effectiveRate) {
    }
}

