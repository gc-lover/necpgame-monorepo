package com.necpgame.backjava.service.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.necpgame.backjava.entity.LoreCharacterCategoryEntity;
import com.necpgame.backjava.entity.LoreCodexEntryEntity;
import com.necpgame.backjava.entity.LoreFactionEntity;
import com.necpgame.backjava.entity.LoreLocationEntity;
import com.necpgame.backjava.entity.LoreTimelineEventEntity;
import com.necpgame.backjava.entity.LoreUniverseEntity;
import com.necpgame.backjava.entity.enums.LoreSearchResultType;
import com.necpgame.backjava.model.CharacterCategory;
import com.necpgame.backjava.model.CodexEntry;
import com.necpgame.backjava.model.Faction;
import com.necpgame.backjava.model.FactionDetailed;
import com.necpgame.backjava.model.FactionDetailedAllOfLeadership;
import com.necpgame.backjava.model.Location;
import com.necpgame.backjava.model.LocationDetailed;
import com.necpgame.backjava.model.LocationDetailedAllOfDistricts;
import com.necpgame.backjava.model.LocationDetailedAllOfEconomy;
import com.necpgame.backjava.model.LoreSearchResult;
import com.necpgame.backjava.model.TimelineEvent;
import com.necpgame.backjava.model.UniverseLore;
import com.necpgame.backjava.model.UniverseLoreSimulationLore;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class LoreMapper {

    private static final TypeReference<List<String>> STRING_LIST = new TypeReference<>() {};
    private static final TypeReference<List<FactionDetailedAllOfLeadership>> LEADERSHIP_LIST = new TypeReference<>() {};
    private static final TypeReference<List<LocationDetailedAllOfDistricts>> DISTRICT_LIST = new TypeReference<>() {};

    private final ObjectMapper objectMapper;

    public LoreMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public UniverseLore toUniverseLore(LoreUniverseEntity entity) {
        UniverseLore universeLore = new UniverseLore()
                .title(entity.getTitle())
                .setting(entity.getSetting())
                .timePeriod(entity.getTimePeriod())
                .majorFactionsCount(entity.getMajorFactionsCount())
                .locationsCount(entity.getLocationsCount());

        universeLore.setKeyEvents(readStringList(entity.getKeyEventsJson()));
        if (entity.getSimulationLoreJson() != null) {
            universeLore.setSimulationLore(readValue(entity.getSimulationLoreJson(), UniverseLoreSimulationLore.class));
        }
        return universeLore;
    }

    public TimelineEvent toTimelineEvent(LoreTimelineEventEntity entity) {
        TimelineEvent timelineEvent = new TimelineEvent()
                .eventId(entity.getEventId())
                .year(entity.getYear())
                .name(entity.getName())
                .description(entity.getDescription())
                .impactLevel(entity.getImpactLevel() != null ? TimelineEvent.ImpactLevelEnum.fromValue(entity.getImpactLevel().name()) : null)
                .type(entity.getType() != null ? TimelineEvent.TypeEnum.fromValue(entity.getType().name()) : null);
        timelineEvent.setRelatedFactions(readStringList(entity.getRelatedFactionsJson()));
        return timelineEvent;
    }

    public List<TimelineEvent> toTimelineEvents(List<LoreTimelineEventEntity> entities) {
        return entities.stream()
                .map(this::toTimelineEvent)
                .collect(Collectors.toList());
    }

    public Faction toFaction(LoreFactionEntity entity) {
        Faction faction = new Faction()
                .factionId(entity.getExternalId())
                .name(entity.getName())
                .region(entity.getRegion())
                .powerLevel(entity.getPowerLevel())
                .descriptionShort(entity.getDescriptionShort());
        if (entity.getType() != null) {
            faction.setType(Faction.TypeEnum.fromValue(entity.getType().name()));
        }
        return faction;
    }

    public List<Faction> toFactions(List<LoreFactionEntity> entities) {
        return entities.stream()
                .map(this::toFaction)
                .collect(Collectors.toList());
    }

    public FactionDetailed toFactionDetailed(LoreFactionEntity entity) {
        FactionDetailed detailed = new FactionDetailed()
                .factionId(entity.getExternalId())
                .name(entity.getName())
                .region(entity.getRegion())
                .powerLevel(entity.getPowerLevel())
                .descriptionShort(entity.getDescriptionShort())
                .fullDescription(entity.getFullDescription())
                .history(entity.getHistory());
        if (entity.getType() != null) {
            detailed.setType(FactionDetailed.TypeEnum.fromValue(entity.getType().name()));
        }
        detailed.setGoals(readStringList(entity.getGoalsJson()));
        detailed.setLeadership(readList(entity.getLeadershipJson(), LEADERSHIP_LIST));
        detailed.setTerritories(readStringList(entity.getTerritoriesJson()));
        detailed.setAllies(readStringList(entity.getAlliesJson()));
        detailed.setEnemies(readStringList(entity.getEnemiesJson()));
        detailed.setResources(readStringList(entity.getResourcesJson()));
        return detailed;
    }

    public Location toLocation(LoreLocationEntity entity) {
        Location location = new Location()
                .locationId(entity.getExternalId())
                .name(entity.getName())
                .region(entity.getRegion())
                .population(entity.getPopulation())
                .dangerLevel(entity.getDangerLevel())
                .descriptionShort(entity.getDescriptionShort());
        if (entity.getType() != null) {
            location.setType(Location.TypeEnum.fromValue(entity.getType().name()));
        }
        return location;
    }

    public List<Location> toLocations(List<LoreLocationEntity> entities) {
        return entities.stream()
                .map(this::toLocation)
                .collect(Collectors.toList());
    }

    public LocationDetailed toLocationDetailed(LoreLocationEntity entity) {
        LocationDetailed detailed = new LocationDetailed()
                .locationId(entity.getExternalId())
                .name(entity.getName())
                .region(entity.getRegion())
                .population(entity.getPopulation())
                .dangerLevel(entity.getDangerLevel())
                .descriptionShort(entity.getDescriptionShort())
                .fullDescription(entity.getFullDescription())
                .history(entity.getHistory());
        if (entity.getType() != null) {
            detailed.setType(LocationDetailed.TypeEnum.fromValue(entity.getType().name()));
        }
        detailed.setDistricts(readList(entity.getDistrictsJson(), DISTRICT_LIST));
        detailed.setControllingFactions(readStringList(entity.getControllingFactionsJson()));
        detailed.setPointsOfInterest(readStringList(entity.getPointsOfInterestJson()));
        if (entity.getEconomyJson() != null) {
            detailed.setEconomy(readValue(entity.getEconomyJson(), LocationDetailedAllOfEconomy.class));
        }
        return detailed;
    }

    public CharacterCategory toCharacterCategory(LoreCharacterCategoryEntity entity) {
        CharacterCategory category = new CharacterCategory()
                .categoryId(entity.getCategoryId())
                .name(entity.getName())
                .description(entity.getDescription())
                .role(entity.getRole());
        category.setExampleCharacters(readStringList(entity.getExampleCharactersJson()));
        return category;
    }

    public CodexEntry toCodexEntry(LoreCodexEntryEntity entity, boolean unlocked) {
        CodexEntry codexEntry = new CodexEntry()
                .entryId(entity.getEntryId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .unlockCondition(entity.getUnlockCondition())
                .unlocked(unlocked || entity.isDefaultUnlocked());
        if (entity.getCategory() != null) {
            codexEntry.setCategory(CodexEntry.CategoryEnum.fromValue(entity.getCategory().name()));
        }
        codexEntry.setRelatedEntries(readStringList(entity.getRelatedEntriesJson()));
        return codexEntry;
    }

    public LoreSearchResult toSearchResult(LoreSearchResultType type, String id, String name, String description, double score) {
        LoreSearchResult.ResultTypeEnum resultType = LoreSearchResult.ResultTypeEnum.fromValue(type.name());
        LoreSearchResult result = new LoreSearchResult()
                .resultType(resultType)
                .id(id)
                .name(name)
                .description(description);
        BigDecimal relevance = BigDecimal.valueOf(score).setScale(2, RoundingMode.HALF_UP);
        result.setRelevanceScore(relevance);
        return result;
    }

    private List<String> readStringList(String json) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        return readList(json, STRING_LIST);
    }

    private <T> List<T> readList(String json, TypeReference<List<T>> typeReference) {
        if (json == null || json.isBlank()) {
            return Collections.emptyList();
        }
        try {
            List<T> items = objectMapper.readValue(json, typeReference);
            return items == null ? Collections.emptyList() : items.stream().filter(Objects::nonNull).collect(Collectors.toList());
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to parse JSON array", e);
        }
    }

    private <T> T readValue(String json, Class<T> clazz) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to parse JSON value", e);
        }
    }
}
