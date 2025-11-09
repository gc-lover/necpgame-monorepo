package com.necpgame.backjava.service;

import com.necpgame.backjava.model.AddGuildMemberRequest;
import com.necpgame.backjava.model.CreateGuildRequest;
import com.necpgame.backjava.model.Error;
import com.necpgame.backjava.model.GetGuildMembers200Response;
import com.necpgame.backjava.model.ListTradingGuilds200Response;
import org.springframework.lang.Nullable;
import com.necpgame.backjava.model.TradingGuild;
import com.necpgame.backjava.model.TradingGuildDetailed;
import java.util.UUID;
import com.necpgame.backjava.model.UpgradeGuildRequest;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for TradingGuildsService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface TradingGuildsService {

    /**
     * POST /gameplay/economy/trading-guilds/{guild_id}/members : Добавить члена гильдии
     *
     * @param guildId  (required)
     * @param addGuildMemberRequest  (required)
     * @return Void
     */
    Void addGuildMember(UUID guildId, AddGuildMemberRequest addGuildMemberRequest);

    /**
     * POST /gameplay/economy/trading-guilds : Создать торговую гильдию
     *
     * @param createGuildRequest  (required)
     * @return TradingGuild
     */
    TradingGuild createTradingGuild(CreateGuildRequest createGuildRequest);

    /**
     * GET /gameplay/economy/trading-guilds/{guild_id}/members : Получить членов гильдии
     *
     * @param guildId  (required)
     * @return GetGuildMembers200Response
     */
    GetGuildMembers200Response getGuildMembers(UUID guildId);

    /**
     * GET /gameplay/economy/trading-guilds/{guild_id} : Получить информацию о гильдии
     *
     * @param guildId  (required)
     * @return TradingGuildDetailed
     */
    TradingGuildDetailed getTradingGuild(UUID guildId);

    /**
     * GET /gameplay/economy/trading-guilds : Получить список торговых гильдий
     *
     * @param type  (optional)
     * @param minLevel  (optional)
     * @param region  (optional)
     * @param page Номер страницы (начинается с 1) (optional, default to 1)
     * @param pageSize Количество элементов на странице (optional, default to 20)
     * @return ListTradingGuilds200Response
     */
    ListTradingGuilds200Response listTradingGuilds(String type, Integer minLevel, String region, Integer page, Integer pageSize);

    /**
     * POST /gameplay/economy/trading-guilds/{guild_id}/upgrade : Улучшить гильдию
     *
     * @param guildId  (required)
     * @param upgradeGuildRequest  (required)
     * @return Void
     */
    Void upgradeGuild(UUID guildId, UpgradeGuildRequest upgradeGuildRequest);
}

