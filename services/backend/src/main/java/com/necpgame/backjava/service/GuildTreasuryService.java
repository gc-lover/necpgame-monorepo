package com.necpgame.backjava.service;

import com.necpgame.backjava.model.ContributeToTreasury200Response;
import com.necpgame.backjava.model.ContributionRequest;
import com.necpgame.backjava.model.DistributeProfits200Response;
import com.necpgame.backjava.model.DistributeProfitsRequest;
import com.necpgame.backjava.model.Error;
import com.necpgame.backjava.model.GuildTreasury;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;

/**
 * Service interface for GuildTreasuryService.
 * Generated from OpenAPI specification.
 * 
 * This is a service interface that should be implemented by a service implementation class.
 */
@Validated
public interface GuildTreasuryService {

    /**
     * POST /gameplay/economy/trading-guilds/{guild_id}/treasury/contribute : Внести вклад в казну
     *
     * @param guildId  (required)
     * @param contributionRequest  (required)
     * @return ContributeToTreasury200Response
     */
    ContributeToTreasury200Response contributeToTreasury(UUID guildId, ContributionRequest contributionRequest);

    /**
     * POST /gameplay/economy/trading-guilds/{guild_id}/profits/distribute : Распределить прибыль
     * Только для Guild Master/Treasurer
     *
     * @param guildId  (required)
     * @param distributeProfitsRequest  (required)
     * @return DistributeProfits200Response
     */
    DistributeProfits200Response distributeProfits(UUID guildId, DistributeProfitsRequest distributeProfitsRequest);

    /**
     * GET /gameplay/economy/trading-guilds/{guild_id}/treasury : Получить состояние казны
     *
     * @param guildId  (required)
     * @return GuildTreasury
     */
    GuildTreasury getGuildTreasury(UUID guildId);
}

