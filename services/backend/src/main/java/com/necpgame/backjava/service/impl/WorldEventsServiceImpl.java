package com.necpgame.backjava.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.EconomicMultiplierEntity;
import com.necpgame.backjava.entity.WorldEventCharacterEffectEntity;
import com.necpgame.backjava.entity.WorldEventEffectEntity;
import com.necpgame.backjava.entity.WorldEventEntity;
import com.necpgame.backjava.entity.WorldEraEntity;
import com.necpgame.backjava.entity.enums.WorldEventType;
import com.necpgame.backjava.model.EconomicMultipliers;
import com.necpgame.backjava.model.EconomicMultipliersTradeRestrictionsInner;
import com.necpgame.backjava.model.EventEffect;
import com.necpgame.backjava.model.GetActiveWorldEvents200Response;
import com.necpgame.backjava.model.GetCharacterAffectedEvents200Response;
import com.necpgame.backjava.model.PaginationMeta;
import com.necpgame.backjava.model.WorldEvent;
import com.necpgame.backjava.model.WorldEventDetailed;
import com.necpgame.backjava.repository.EconomicMultiplierRepository;
import com.necpgame.backjava.repository.WorldEventCharacterEffectRepository;
import com.necpgame.backjava.repository.WorldEventEffectRepository;
import com.necpgame.backjava.repository.WorldEventRepository;
import com.necpgame.backjava.repository.WorldEraRepository;
import com.necpgame.backjava.repository.specification.WorldEventSpecifications;
import com.necpgame.backjava.service.WorldEventsService;
import com.necpgame.backjava.service.mapper.WorldEventMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class WorldEventsServiceImpl implements WorldEventsService {

    private static final TypeReference<Map<String, BigDecimal>> STRING_DECIMAL_MAP = new TypeReference<>() {
    };
    private static final TypeReference<List<TradeRestrictionPayload>> TRADE_RESTRICTIONS = new TypeReference<>() {
    };

    private final WorldEventRepository worldEventRepository;
    private final WorldEventEffectRepository worldEventEffectRepository;
    private final WorldEventCharacterEffectRepository worldEventCharacterEffectRepository;
    private final WorldEraRepository worldEraRepository;
    private final EconomicMultiplierRepository economicMultiplierRepository;
    private final WorldEventMapper worldEventMapper;
    private final ObjectMapper objectMapper;

    public WorldEventsServiceImpl(WorldEventRepository worldEventRepository,
                                  WorldEventEffectRepository worldEventEffectRepository,
                                  WorldEventCharacterEffectRepository worldEventCharacterEffectRepository,
                                  WorldEraRepository worldEraRepository,
                                  EconomicMultiplierRepository economicMultiplierRepository,
                                  WorldEventMapper worldEventMapper,
                                  ObjectMapper objectMapper) {
        this.worldEventRepository = worldEventRepository;
        this.worldEventEffectRepository = worldEventEffectRepository;
        this.worldEventCharacterEffectRepository = worldEventCharacterEffectRepository;
        this.worldEraRepository = worldEraRepository;
        this.economicMultiplierRepository = economicMultiplierRepository;
        this.worldEventMapper = worldEventMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public GetActiveWorldEvents200Response getActiveWorldEvents(String era, String eventType, Integer page, Integer pageSize) {
        int requestedPage = page != null && page > 0 ? page - 1 : 0;
        int requestedSize = pageSize != null && pageSize > 0 ? pageSize : 20;

        Pageable pageable = PageRequest.of(requestedPage, requestedSize, Sort.by(Sort.Direction.DESC, "startDate"));

        Specification<WorldEventEntity> specification = Specification.where(WorldEventSpecifications.isActive());
        if (era != null && !era.isBlank()) {
            specification = specification.and(WorldEventSpecifications.byEra(era));
        }
        WorldEventType filterType = parseEventType(eventType);
        if (filterType != null) {
            specification = specification.and(WorldEventSpecifications.byType(filterType));
        }

        Page<WorldEventEntity> pageResult = worldEventRepository.findAll(specification, pageable);
        List<WorldEvent> items = pageResult.getContent().stream()
                .map(worldEventMapper::toWorldEvent)
                .collect(Collectors.toList());

        PaginationMeta meta = new PaginationMeta()
                .page(requestedPage + 1)
                .pageSize(requestedSize)
                .total(Math.toIntExact(pageResult.getTotalElements()))
                .totalPages(pageResult.getTotalPages())
                .hasNext(pageResult.hasNext())
                .hasPrev(pageResult.hasPrevious());

        String currentEra = worldEraRepository.findFirstByCurrentTrue()
                .map(WorldEraEntity::getEra)
                .orElse(null);

        return new GetActiveWorldEvents200Response(items, meta)
                .currentEra(currentEra);
    }

    @Override
    public GetCharacterAffectedEvents200Response getCharacterAffectedEvents(UUID characterId) {
        List<WorldEventCharacterEffectEntity> characterEffects = worldEventCharacterEffectRepository.findByIdCharacterId(characterId);
        if (characterEffects.isEmpty()) {
            return new GetCharacterAffectedEvents200Response().activeEffects(Collections.emptyList());
        }

        List<EventEffect> effects = characterEffects.stream()
                .map(WorldEventCharacterEffectEntity::getEffect)
                .map(worldEventMapper::toEventEffect)
                .collect(Collectors.toList());

        return new GetCharacterAffectedEvents200Response().activeEffects(effects);
    }

    @Override
    public EconomicMultipliers getEconomicMultipliers() {
        EconomicMultiplierEntity multiplierEntity = economicMultiplierRepository.findFirstByOrderByUpdatedAtDesc()
                .orElse(null);

        if (multiplierEntity == null) {
            return new EconomicMultipliers();
        }

        EconomicMultipliers multipliers = new EconomicMultipliers();
        multipliers.setPriceMultipliers(parseStringDecimalMap(multiplierEntity.getPriceMultipliersJson()));
        multipliers.setCurrencyExchangeRates(parseStringDecimalMap(multiplierEntity.getCurrencyExchangeRatesJson()));
        multipliers.setTradeRestrictions(parseTradeRestrictions(multiplierEntity.getTradeRestrictionsJson()));
        return multipliers;
    }

    @Override
    public WorldEventDetailed getWorldEvent(String eventId) {
        UUID eventUuid = parseUuid(eventId, "event_id");
        WorldEventEntity entity = worldEventRepository.findById(eventUuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Событие не найдено"));
        List<WorldEventEffectEntity> effects = worldEventEffectRepository.findByEventId(eventUuid);
        return worldEventMapper.toWorldEventDetailed(entity, effects);
    }

    private WorldEventType parseEventType(String eventType) {
        if (eventType == null || eventType.isBlank()) {
            return null;
        }
        try {
            return WorldEventType.valueOf(eventType);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректный параметр event_type");
        }
    }

    private UUID parseUuid(String value, String fieldName) {
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Некорректное значение " + fieldName);
        }
    }

    private Map<String, BigDecimal> parseStringDecimalMap(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyMap();
        }
        try {
            return objectMapper.readValue(json, STRING_DECIMAL_MAP);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Не удалось разобрать числовую карту из JSON", ex);
        }
    }

    private List<EconomicMultipliersTradeRestrictionsInner> parseTradeRestrictions(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            List<TradeRestrictionPayload> payloads = objectMapper.readValue(json, TRADE_RESTRICTIONS);
            return payloads.stream()
                    .map(payload -> new EconomicMultipliersTradeRestrictionsInner()
                            .itemCategory(payload.itemCategory)
                            .restrictionType(EconomicMultipliersTradeRestrictionsInner.RestrictionTypeEnum.fromValue(payload.restrictionType))
                            .multiplier(payload.multiplier))
                    .collect(Collectors.toList());
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Не удалось разобрать торговые ограничения", ex);
        }
    }

    private record TradeRestrictionPayload(String itemCategory, String restrictionType, BigDecimal multiplier) {
    }
}

