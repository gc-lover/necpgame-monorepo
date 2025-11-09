package com.necpgame.backjava.service.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.FactionAiSliderEntity;
import com.necpgame.backjava.entity.TechnologyAccessEntity;
import com.necpgame.backjava.entity.WorldEraEntity;
import com.necpgame.backjava.entity.WorldEraMechanicsEntity;
import com.necpgame.backjava.model.DCScaling;
import com.necpgame.backjava.model.DCScalingExampleChallengesInner;
import com.necpgame.backjava.model.DCScalingModifiers;
import com.necpgame.backjava.model.EraInfo;
import com.necpgame.backjava.model.EraMechanics;
import com.necpgame.backjava.model.EraMechanicsEconomicState;
import com.necpgame.backjava.model.EraMechanicsSocialMechanics;
import com.necpgame.backjava.model.FactionAISlider;
import com.necpgame.backjava.model.GetFactionAISliders200Response;
import com.necpgame.backjava.model.GetTechnologyAccess200Response;
import com.necpgame.backjava.model.TechnologyAccess;
import com.necpgame.backjava.repository.FactionAiSliderRepository;
import com.necpgame.backjava.repository.TechnologyAccessRepository;
import com.necpgame.backjava.repository.WorldEraMechanicsRepository;
import com.necpgame.backjava.repository.WorldEraRepository;
import com.necpgame.backjava.service.EraMechanicsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EraMechanicsServiceImpl implements EraMechanicsService {

    private static final TypeReference<List<String>> STRING_LIST = new TypeReference<>() {
    };
    private static final TypeReference<List<ExampleChallengePayload>> EXAMPLE_CHALLENGES = new TypeReference<>() {
    };
    private static final TypeReference<Map<String, Integer>> RELATIONS_MAP = new TypeReference<>() {
    };

    private final WorldEraRepository worldEraRepository;
    private final WorldEraMechanicsRepository worldEraMechanicsRepository;
    private final FactionAiSliderRepository factionAiSliderRepository;
    private final TechnologyAccessRepository technologyAccessRepository;
    private final ObjectMapper objectMapper;

    public EraMechanicsServiceImpl(WorldEraRepository worldEraRepository,
                                   WorldEraMechanicsRepository worldEraMechanicsRepository,
                                   FactionAiSliderRepository factionAiSliderRepository,
                                   TechnologyAccessRepository technologyAccessRepository,
                                   ObjectMapper objectMapper) {
        this.worldEraRepository = worldEraRepository;
        this.worldEraMechanicsRepository = worldEraMechanicsRepository;
        this.factionAiSliderRepository = factionAiSliderRepository;
        this.technologyAccessRepository = technologyAccessRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public EraInfo getCurrentEra() {
        WorldEraEntity eraEntity = worldEraRepository.findFirstByCurrentTrue()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Текущая эра не настроена"));
        return mapEraInfo(eraEntity);
    }

    @Override
    public DCScaling getDCScaling(String era) {
        WorldEraMechanicsEntity mechanics = worldEraMechanicsRepository.findById(requireEra(era))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Механики эры не найдены"));
        return mapDcScaling(mechanics);
    }

    @Override
    public EraMechanics getEraMechanics(String era) {
        WorldEraMechanicsEntity mechanicsEntity = worldEraMechanicsRepository.findById(requireEra(era))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Механики эры не найдены"));
        EraMechanics mechanics = new EraMechanics();
        mechanics.setEra(mechanicsEntity.getEra());
        mechanics.setDcScaling(mapDcScaling(mechanicsEntity));
        mechanics.setEconomicState(mapEconomicState(mechanicsEntity.getEconomicStateJson()));
        mechanics.setTechnologyRestrictions(readStringList(mechanicsEntity.getTechnologyRestrictionsJson()));
        mechanics.setSocialMechanics(mapSocialMechanics(mechanicsEntity.getSocialMechanicsJson()));
        return mechanics;
    }

    @Override
    public GetFactionAISliders200Response getFactionAISliders() {
        List<FactionAISlider> sliders = factionAiSliderRepository.findAll().stream()
                .map(this::mapFactionSlider)
                .collect(Collectors.toList());
        return new GetFactionAISliders200Response().factions(sliders);
    }

    @Override
    public GetTechnologyAccess200Response getTechnologyAccess() {
        String currentEra = worldEraRepository.findFirstByCurrentTrue()
                .map(WorldEraEntity::getEra)
                .orElse(null);

        List<TechnologyAccessEntity> entities = technologyAccessRepository.findByAvailableTrue();
        List<TechnologyAccess> items = entities.stream()
                .filter(entity -> currentEra == null || entity.getRequiredEra() == null || Objects.equals(entity.getRequiredEra(), currentEra))
                .map(this::mapTechnologyAccess)
                .collect(Collectors.toList());

        return new GetTechnologyAccess200Response().availableTech(items);
    }

    private EraInfo mapEraInfo(WorldEraEntity entity) {
        EraInfo info = new EraInfo();
        info.setEra(entity.getEra());
        info.setName(entity.getName());
        info.setDescription(entity.getDescription());
        info.setKeyFeatures(readStringList(entity.getKeyFeaturesJson()));
        info.setMajorFactions(readStringList(entity.getMajorFactionsJson()));
        info.setTechnologyLevel(entity.getTechnologyLevel());
        if (entity.getDangerLevel() != null) {
            info.setDangerLevel(EraInfo.DangerLevelEnum.fromValue(entity.getDangerLevel().name()));
        }
        info.setActiveEventsCount(entity.getActiveEventsCount());
        return info;
    }

    private DCScaling mapDcScaling(WorldEraMechanicsEntity mechanicsEntity) {
        DCScaling scaling = new DCScaling();
        scaling.setEra(mechanicsEntity.getEra());
        scaling.setBaseDc(mechanicsEntity.getBaseDc());

        DCScalingModifiers modifiers = new DCScalingModifiers();
        modifiers.setCombat(mechanicsEntity.getCombatModifier());
        modifiers.setSocial(mechanicsEntity.getSocialModifier());
        modifiers.setHacking(mechanicsEntity.getHackingModifier());
        modifiers.setCrafting(mechanicsEntity.getCraftingModifier());
        scaling.setModifiers(modifiers);

        List<DCScalingExampleChallengesInner> challenges = readExampleChallenges(mechanicsEntity.getExampleChallengesJson());
        scaling.setExampleChallenges(challenges);

        return scaling;
    }

    private EraMechanicsEconomicState mapEconomicState(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            EconomicStatePayload payload = objectMapper.readValue(json, EconomicStatePayload.class);
            return new EraMechanicsEconomicState()
                    .inflationRate(payload.inflationRate)
                    .averagePricesMultiplier(payload.averagePricesMultiplier)
                    .currencyStability(payload.currencyStability);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Не удалось разобрать экономическое состояние эры", ex);
        }
    }

    private EraMechanicsSocialMechanics mapSocialMechanics(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            SocialMechanicsPayload payload = objectMapper.readValue(json, SocialMechanicsPayload.class);
            return new EraMechanicsSocialMechanics()
                    .reputationVolatility(payload.reputationVolatility)
                    .factionHostilityMultiplier(payload.factionHostilityMultiplier);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Не удалось разобрать социальные механики", ex);
        }
    }

    private List<String> readStringList(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, STRING_LIST);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Не удалось разобрать список строк", ex);
        }
    }

    private List<DCScalingExampleChallengesInner> readExampleChallenges(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            List<ExampleChallengePayload> payloads = objectMapper.readValue(json, EXAMPLE_CHALLENGES);
            return payloads.stream()
                    .map(payload -> new DCScalingExampleChallengesInner()
                            .challenge(payload.challenge)
                            .dc(payload.dc))
                    .collect(Collectors.toList());
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Не удалось разобрать примеры испытаний", ex);
        }
    }

    private FactionAISlider mapFactionSlider(FactionAiSliderEntity entity) {
        FactionAISlider slider = new FactionAISlider();
        slider.setFaction(entity.getFaction());
        slider.setInfluence(toFloat(entity.getInfluence()));
        slider.setAggression(toFloat(entity.getAggression()));
        slider.setWealth(toFloat(entity.getWealth()));
        slider.setTechnology(toFloat(entity.getTechnology()));
        slider.setRelations(readRelations(entity.getRelationsJson()));
        return slider;
    }

    private Map<String, Integer> readRelations(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyMap();
        }
        try {
            return objectMapper.readValue(json, RELATIONS_MAP);
        } catch (JsonProcessingException ex) {
            throw new IllegalStateException("Не удалось разобрать отношения фракций", ex);
        }
    }

    private TechnologyAccess mapTechnologyAccess(TechnologyAccessEntity entity) {
        TechnologyAccess access = new TechnologyAccess();
        access.setTechnologyId(entity.getTechnologyId().toString());
        access.setName(entity.getName());
        access.setCategory(TechnologyAccess.CategoryEnum.fromValue(entity.getCategory().name()));
        access.setAvailable(entity.isAvailable());
        access.setRequiredEra(entity.getRequiredEra());
        access.setRestrictedFactions(readStringList(entity.getRestrictedFactionsJson()));
        return access;
    }

    private String requireEra(String era) {
        if (era == null || era.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Параметр era обязателен");
        }
        return era;
    }

    private Float toFloat(BigDecimal value) {
        return value == null ? null : value.floatValue();
    }

    private record EconomicStatePayload(@JsonProperty("inflation_rate") BigDecimal inflationRate,
                                        @JsonProperty("average_prices_multiplier") BigDecimal averagePricesMultiplier,
                                        @JsonProperty("currency_stability") String currencyStability) {
    }

    private record SocialMechanicsPayload(@JsonProperty("reputation_volatility") BigDecimal reputationVolatility,
                                          @JsonProperty("faction_hostility_multiplier") BigDecimal factionHostilityMultiplier) {
    }

    private record ExampleChallengePayload(String challenge, Integer dc) {
    }
}

