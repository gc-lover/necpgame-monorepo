package com.necpgame.worldservice.service;

import com.necpgame.worldservice.model.Error;
import com.necpgame.worldservice.model.PlayerOrderEconomicIndex;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for EconomyService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface EconomyService {

    /**
     * GET /economy/player-orders/index : Получить экономические индексы заказов
     * Возвращает текущие экономические индексы, связанные с активностью заказов игроков.
     *
     * @return PlayerOrderEconomicIndex
     */
    PlayerOrderEconomicIndex getPlayerOrderEconomicIndex();
}

