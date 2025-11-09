package com.necpgame.worldservice.service;

import com.necpgame.worldservice.model.Error;
import com.necpgame.worldservice.model.GetFactionPower200Response;
import org.springframework.lang.Nullable;
import com.necpgame.worldservice.model.RegionState;
import com.necpgame.worldservice.model.RegisterWorldImpact200Response;
import com.necpgame.worldservice.model.WorldImpact;
import com.necpgame.worldservice.model.WorldState;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for GameplayService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface GameplayService {

    /**
     * GET /gameplay/world/world-state/faction-power : Получить расстановку сил фракций
     * Возвращает текущую расстановку сил между фракциями
     *
     * @param regionId  (optional)
     * @return GetFactionPower200Response
     */
    GetFactionPower200Response getFactionPower(String regionId);

    /**
     * GET /gameplay/world/world-state/{region_id} : Получить состояние региона
     * Возвращает детальное состояние конкретного региона
     *
     * @param regionId  (required)
     * @return RegionState
     */
    RegionState getRegionState(String regionId);

    /**
     * GET /gameplay/world/world-state : Получить глобальное состояние мира
     * Возвращает текущее состояние мира по категориям. Можно фильтровать по категории, региону, фракции. 
     *
     * @param category  (optional)
     * @param regionId  (optional)
     * @param factionId  (optional)
     * @return WorldState
     */
    WorldState getWorldState(String category, String regionId, String factionId);

    /**
     * POST /gameplay/world/world-state/impact : Зарегистрировать влияние на мир
     * Регистрирует действие игрока, влияющее на состояние мира. Влияние агрегируется от индивидуального к глобальному. 
     *
     * @param worldImpact  (required)
     * @return RegisterWorldImpact200Response
     */
    RegisterWorldImpact200Response registerWorldImpact(WorldImpact worldImpact);
}

