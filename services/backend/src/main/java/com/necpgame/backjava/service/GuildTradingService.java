package com.necpgame.backjava.service;

import com.necpgame.backjava.model.GetGuildQuotas200Response;
import com.necpgame.backjava.model.GetGuildTradeRoutes200Response;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for GuildTradingService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface GuildTradingService {

    /**
     * GET /gameplay/economy/trading-guilds/{guild_id}/quotas : Получить торговые квоты
     *
     * @param guildId  (required)
     * @return GetGuildQuotas200Response
     */
    GetGuildQuotas200Response getGuildQuotas(UUID guildId);

    /**
     * GET /gameplay/economy/trading-guilds/{guild_id}/trade-routes : Получить торговые маршруты гильдии
     *
     * @param guildId  (required)
     * @return GetGuildTradeRoutes200Response
     */
    GetGuildTradeRoutes200Response getGuildTradeRoutes(UUID guildId);
}

