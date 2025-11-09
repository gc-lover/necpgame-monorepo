package com.necpgame.backjava.service;

import com.necpgame.backjava.model.Error;
import com.necpgame.backjava.model.FactionDetailed;
import com.necpgame.backjava.model.GetCharacterCategories200Response;
import com.necpgame.backjava.model.GetCharacterCodex200Response;
import com.necpgame.backjava.model.GetTimeline200Response;
import com.necpgame.backjava.model.ListFactions200Response;
import com.necpgame.backjava.model.ListLocations200Response;
import com.necpgame.backjava.model.LocationDetailed;
import org.springframework.lang.Nullable;
import com.necpgame.backjava.model.SearchLore200Response;
import java.util.UUID;
import com.necpgame.backjava.model.UniverseLore;
import com.necpgame.backjava.model.UnlockCodexEntryRequest;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for LoreService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface LoreService {

    /**
     * GET /lore/characters/categories : Получить категории персонажей
     *
     * @return GetCharacterCategories200Response
     */
    GetCharacterCategories200Response getCharacterCategories();

    /**
     * GET /lore/codex/character/{character_id} : Получить записи codex персонажа
     * Открытые записи в codex
     *
     * @param characterId  (required)
     * @return GetCharacterCodex200Response
     */
    GetCharacterCodex200Response getCharacterCodex(UUID characterId);

    /**
     * GET /lore/factions/{faction_id} : Получить детали фракции
     *
     * @param factionId  (required)
     * @return FactionDetailed
     */
    FactionDetailed getFaction(String factionId);

    /**
     * GET /lore/locations/{location_id} : Получить детали локации
     *
     * @param locationId  (required)
     * @return LocationDetailed
     */
    LocationDetailed getLocation(String locationId);

    /**
     * GET /lore/universe/timeline : Получить временную шкалу
     *
     * @param era  (optional)
     * @param eventType  (optional)
     * @return GetTimeline200Response
     */
    GetTimeline200Response getTimeline(String era, String eventType);

    /**
     * GET /lore/universe : Получить информацию о вселенной
     *
     * @return UniverseLore
     */
    UniverseLore getUniverseLore();

    /**
     * GET /lore/factions : Получить список фракций
     *
     * @param type  (optional)
     * @param region  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return ListFactions200Response
     */
    ListFactions200Response listFactions(String type, String region, Integer page, Integer pageSize);

    /**
     * GET /lore/locations : Получить список локаций
     *
     * @param region  (optional)
     * @param type  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return ListLocations200Response
     */
    ListLocations200Response listLocations(String region, String type, Integer page, Integer pageSize);

    /**
     * GET /lore/search : Поиск по лору
     *
     * @param q  (required)
     * @param category  (optional)
     * @return SearchLore200Response
     */
    SearchLore200Response searchLore(String q, String category);

    /**
     * POST /lore/codex/unlock : Разблокировать запись codex
     *
     * @param unlockCodexEntryRequest  (required)
     * @return Void
     */
    Void unlockCodexEntry(UnlockCodexEntryRequest unlockCodexEntryRequest);
}

