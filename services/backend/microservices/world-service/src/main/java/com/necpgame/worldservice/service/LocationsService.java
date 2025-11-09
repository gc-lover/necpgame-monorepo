package com.necpgame.worldservice.service;

import com.necpgame.worldservice.model.Error;
import com.necpgame.worldservice.model.GetConnectedLocations200Response;
import com.necpgame.worldservice.model.GetLocationActions200Response;
import com.necpgame.worldservice.model.GetLocations200Response;
import com.necpgame.worldservice.model.LocationDetails;
import org.springframework.lang.Nullable;
import com.necpgame.worldservice.model.TravelRequest;
import com.necpgame.worldservice.model.TravelResponse;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for LocationsService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface LocationsService {

    /**
     * GET /locations/{locationId}/connected : Связанные локации
     * Возвращает список локаций, связанных с текущей, с информацией о доступности и времени пути.  **Бизнес-логика:** - Возвращает только связанные локации - Проверяет доступность каждой локации - Возвращает время пути и расстояние - Указывает требования для недоступных локаций  **Источник:** &#x60;.BRAIN/05-technical/ui-main-game.md&#x60; (Раздел 4.1, 4.2) 
     *
     * @param locationId ID локации (required)
     * @param characterId ID персонажа (для проверки доступности) (optional)
     * @return GetConnectedLocations200Response
     */
    GetConnectedLocations200Response getConnectedLocations(String locationId, UUID characterId);

    /**
     * GET /locations/current : Текущая локация персонажа
     * Возвращает текущую локацию персонажа с полным описанием и доступными действиями.  **Бизнес-логика:** - Загружает текущую локацию персонажа из БД - Возвращает атмосферное описание - Возвращает список доступных действий в локации - Возвращает список доступных NPC  **Источник:** &#x60;.BRAIN/05-technical/ui-main-game.md&#x60; (Раздел 1.2) 
     *
     * @param characterId ID персонажа (required)
     * @return LocationDetails
     */
    LocationDetails getCurrentLocation(UUID characterId);

    /**
     * GET /locations/{locationId}/actions : Доступные действия в локации
     * Возвращает список доступных действий в конкретной локации для персонажа.  **Бизнес-логика:** - Возвращает только действия, доступные для персонажа - Проверяет требования (уровень, навыки, предметы, квесты) - Возвращает информацию о том, почему действие недоступно  **Источник:** &#x60;.BRAIN/05-technical/ui-main-game.md&#x60; (Раздел 2.1) 
     *
     * @param locationId ID локации (required)
     * @param characterId ID персонажа (required)
     * @return GetLocationActions200Response
     */
    GetLocationActions200Response getLocationActions(String locationId, UUID characterId);

    /**
     * GET /locations/{locationId} : Детальная информация о локации
     * Возвращает детальную информацию о конкретной локации, включая атмосферное описание, точки интереса и доступных NPC.  **Бизнес-логика:** - Проверяет доступность локации для персонажа - Возвращает полное описание и атмосферу - Включает список NPC и точек интереса  **Источник:** &#x60;.BRAIN/05-technical/ui-main-game.md&#x60; (Раздел 1.2) 
     *
     * @param locationId ID локации (required)
     * @param characterId ID персонажа (required)
     * @return LocationDetails
     */
    LocationDetails getLocationDetails(String locationId, UUID characterId);

    /**
     * GET /locations : Список всех доступных локаций
     * Возвращает список всех доступных локаций с возможностью фильтрации.  **Бизнес-логика:** - Возвращаются только локации, доступные для персонажа (по уровню и квестам) - Можно фильтровать по региону и уровню опасности - Содержит краткую информацию о каждой локации  **Источник:** &#x60;.BRAIN/05-technical/ui-main-game.md&#x60; (Раздел 4.1) 
     *
     * @param characterId ID персонажа (для проверки доступности локаций) (required)
     * @param region Фильтр по региону (optional)
     * @param dangerLevel Фильтр по уровню опасности (optional)
     * @param minLevel Фильтр по минимальному уровню персонажа (optional)
     * @return GetLocations200Response
     */
    GetLocations200Response getLocations(UUID characterId, String region, String dangerLevel, Integer minLevel);

    /**
     * POST /locations/travel : Перемещение в другую локацию
     * Перемещает персонажа в другую локацию. Проверяет доступность, расходует энергию и время.  **Бизнес-логика:** - Проверяет доступность целевой локации (уровень, квесты, связанность) - Проверяет наличие энергии у персонажа - Расходует энергию и время на перемещение - Может генерировать случайные события во время перемещения - Fast travel мгновенное, если локация открыта  **Источник:** &#x60;.BRAIN/05-technical/ui-main-game.md&#x60; (Раздел 4.2) 
     *
     * @param travelRequest  (required)
     * @return TravelResponse
     */
    TravelResponse travelToLocation(TravelRequest travelRequest);
}

