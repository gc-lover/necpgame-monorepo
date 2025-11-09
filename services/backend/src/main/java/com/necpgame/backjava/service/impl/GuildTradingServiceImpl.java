package com.necpgame.backjava.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.TradingGuildEntity;
import com.necpgame.backjava.entity.TradingGuildQuotaEntity;
import com.necpgame.backjava.entity.TradingGuildRouteEntity;
import com.necpgame.backjava.entity.enums.TradeRouteDangerLevel;
import com.necpgame.backjava.model.GetGuildQuotas200Response;
import com.necpgame.backjava.model.GetGuildTradeRoutes200Response;
import com.necpgame.backjava.model.TradeRoute;
import com.necpgame.backjava.model.TradingQuota;
import com.necpgame.backjava.repository.TradingGuildQuotaRepository;
import com.necpgame.backjava.repository.TradingGuildRepository;
import com.necpgame.backjava.repository.TradingGuildRouteRepository;
import com.necpgame.backjava.service.GuildTradingService;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuildTradingServiceImpl implements GuildTradingService {

    private static final TypeReference<List<String>> STRING_LIST_TYPE = new TypeReference<>() {
    };

    private final TradingGuildRepository tradingGuildRepository;
    private final TradingGuildRouteRepository tradingGuildRouteRepository;
    private final TradingGuildQuotaRepository tradingGuildQuotaRepository;
    private final ObjectMapper objectMapper;

    @Override
    public GetGuildTradeRoutes200Response getGuildTradeRoutes(UUID guildId) {
        findGuildOrThrow(guildId);
        List<TradingGuildRouteEntity> routes = tradingGuildRouteRepository.findByGuildId(guildId);

        List<TradeRoute> active = routes.stream()
            .filter(route -> !route.isExclusive())
            .map(this::mapRoute)
            .collect(Collectors.toList());

        List<TradeRoute> exclusive = routes.stream()
            .filter(TradingGuildRouteEntity::isExclusive)
            .map(this::mapRoute)
            .collect(Collectors.toList());

        return new GetGuildTradeRoutes200Response()
            .activeRoutes(active)
            .exclusiveRoutes(exclusive);
    }

    @Override
    public GetGuildQuotas200Response getGuildQuotas(UUID guildId) {
        findGuildOrThrow(guildId);
        List<TradingGuildQuotaEntity> quotas = tradingGuildQuotaRepository.findByGuildId(guildId);

        List<TradingQuota> models = quotas.stream()
            .map(this::mapQuota)
            .collect(Collectors.toList());

        return new GetGuildQuotas200Response()
            .quotas(models);
    }

    private TradingGuildEntity findGuildOrThrow(UUID guildId) {
        return tradingGuildRepository.findById(guildId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guild not found"));
    }

    private TradeRoute mapRoute(TradingGuildRouteEntity entity) {
        TradeRoute.DangerLevelEnum danger = null;
        TradeRouteDangerLevel level = entity.getDangerLevel();
        if (level != null) {
            danger = TradeRoute.DangerLevelEnum.fromValue(level.name());
        }

        List<String> goods = readGoods(entity.getGoodsJson());

        return new TradeRoute()
            .routeId(entity.getRouteId() != null ? entity.getRouteId().toString() : null)
            .name(entity.getName())
            .origin(entity.getOrigin())
            .destination(entity.getDestination())
            .goods(goods)
            .profitMargin(entity.getProfitMargin() != null ? entity.getProfitMargin().floatValue() : null)
            .isExclusive(entity.isExclusive())
            .dangerLevel(danger);
    }

    private TradingQuota mapQuota(TradingGuildQuotaEntity entity) {
        return new TradingQuota()
            .itemCategory(entity.getItemCategory())
            .maxQuantityPerWeek(entity.getMaxQuantityPerWeek())
            .currentUsed(entity.getCurrentUsed())
            .resetsAt(entity.getResetsAt() != null ? OffsetDateTime.ofInstant(entity.getResetsAt(), ZoneOffset.UTC) : null);
    }

    private List<String> readGoods(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, STRING_LIST_TYPE);
        } catch (JsonProcessingException ex) {
            return Collections.emptyList();
        }
    }
}

