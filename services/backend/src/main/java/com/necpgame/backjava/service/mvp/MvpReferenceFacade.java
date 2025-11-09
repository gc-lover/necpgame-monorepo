package com.necpgame.backjava.service.mvp;

import com.necpgame.backjava.entity.mvp.MvpContentOverviewEntity;
import com.necpgame.backjava.entity.mvp.MvpEndpointEntity;
import com.necpgame.backjava.entity.mvp.MvpStarterFactionEntity;
import com.necpgame.backjava.entity.mvp.MvpStarterItemEntity;
import com.necpgame.backjava.entity.mvp.MvpStarterLocationEntity;
import com.necpgame.backjava.entity.mvp.MvpStarterNpcEntity;
import com.necpgame.backjava.entity.mvp.MvpStarterQuestEntity;
import com.necpgame.backjava.mapper.MvpContentMapper;
import com.necpgame.backjava.model.EndpointDefinition;
import com.necpgame.backjava.model.GetMVPEndpoints200Response;
import com.necpgame.backjava.model.GetMVPModels200Response;
import com.necpgame.backjava.model.InitialGameData;
import com.necpgame.backjava.model.InitialGameDataNpcsInner;
import com.necpgame.backjava.model.InitialGameDataStarterItemsInner;
import com.necpgame.backjava.model.ModelDefinition;
import com.necpgame.backjava.repository.mvp.MvpContentOverviewRepository;
import com.necpgame.backjava.repository.mvp.MvpEndpointRepository;
import com.necpgame.backjava.repository.mvp.MvpModelRepository;
import com.necpgame.backjava.repository.mvp.MvpStarterFactionRepository;
import com.necpgame.backjava.repository.mvp.MvpStarterItemRepository;
import com.necpgame.backjava.repository.mvp.MvpStarterLocationRepository;
import com.necpgame.backjava.repository.mvp.MvpStarterNpcRepository;
import com.necpgame.backjava.repository.mvp.MvpStarterQuestRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MvpReferenceFacade {

    private final MvpEndpointRepository endpointRepository;
    private final MvpModelRepository modelRepository;
    private final MvpStarterItemRepository starterItemRepository;
    private final MvpStarterQuestRepository starterQuestRepository;
    private final MvpStarterLocationRepository starterLocationRepository;
    private final MvpStarterNpcRepository starterNpcRepository;
    private final MvpStarterFactionRepository starterFactionRepository;
    private final MvpContentOverviewRepository contentOverviewRepository;
    private final MvpContentMapper mapper;

    public GetMVPEndpoints200Response getEndpoints() {
        List<MvpEndpointEntity> endpoints = endpointRepository.findAllByOrderByCategoryAscEndpointAsc();
        List<EndpointDefinition> dto = endpoints.stream().map(mapper::toEndpointDefinition).toList();
        Map<String, Integer> categories = mapper.toCategoryMap(endpoints).entrySet().stream()
            .collect(HashMap::new, (map, entry) -> map.put(entry.getKey(), Math.toIntExact(entry.getValue())), Map::putAll);
        return new GetMVPEndpoints200Response()
            .endpoints(dto)
            .totalCount(dto.size())
            .categories(categories);
    }

    public GetMVPModels200Response getModels() {
        List<ModelDefinition> models = modelRepository.findAllByOrderByModelNameAsc().stream()
            .map(mapper::toModelDefinition)
            .toList();
        return new GetMVPModels200Response().models(models);
    }

    public InitialGameData getInitialData() {
        List<InitialGameDataStarterItemsInner> starterItems = starterItemRepository.findAllByOrderByItemIdAsc().stream()
            .map(this::toStarterItem)
            .toList();

        List<String> starterQuests = starterQuestRepository.findAllByOrderByQuestCodeAsc().stream()
            .map(MvpStarterQuestEntity::getQuestCode)
            .toList();

        List<String> starterLocations = starterLocationRepository.findAllByOrderByLocationNameAsc().stream()
            .map(MvpStarterLocationEntity::getLocationName)
            .toList();

        List<InitialGameDataNpcsInner> npcs = starterNpcRepository.findAllByOrderByNameAsc().stream()
            .map(this::toNpc)
            .toList();

        List<Object> factions = starterFactionRepository.findAllByOrderByNameAsc().stream()
            .map(this::toFactionMap)
            .map(map -> (Object) map)
            .toList();

        return new InitialGameData()
            .starterItems(starterItems)
            .starterQuests(starterQuests)
            .starterLocations(starterLocations)
            .npcs(npcs)
            .factions(factions);
    }

    public MvpContentOverviewEntity requireOverview(String period) {
        Optional<MvpContentOverviewEntity> overviewOptional = period == null
            ? contentOverviewRepository.findTopByOrderByPeriodAsc()
            : contentOverviewRepository.findByPeriod(period);
        return overviewOptional
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Контент за указанный период не найден"));
    }

    private InitialGameDataStarterItemsInner toStarterItem(MvpStarterItemEntity entity) {
        return new InitialGameDataStarterItemsInner()
            .itemId(entity.getItemId())
            .quantity(entity.getQuantity());
    }

    private InitialGameDataNpcsInner toNpc(MvpStarterNpcEntity entity) {
        String locationName = Optional.ofNullable(entity.getLocation())
            .map(MvpStarterLocationEntity::getLocationName)
            .orElse(null);
        return new InitialGameDataNpcsInner()
            .npcId(entity.getNpcId())
            .name(entity.getName())
            .location(locationName)
            .role(entity.getRole());
    }

    private Map<String, Object> toFactionMap(MvpStarterFactionEntity entity) {
        Map<String, Object> faction = new HashMap<>();
        faction.put("faction_id", entity.getFactionId());
        faction.put("name", entity.getName());
        faction.put("description", entity.getDescription());
        return faction;
    }
}

