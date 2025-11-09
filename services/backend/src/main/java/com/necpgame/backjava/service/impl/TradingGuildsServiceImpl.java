package com.necpgame.backjava.service.impl;

import com.necpgame.backjava.model.AddGuildMemberRequest;
import com.necpgame.backjava.model.CreateGuildRequest;
import com.necpgame.backjava.model.GetGuildMembers200Response;
import com.necpgame.backjava.model.ListTradingGuilds200Response;
import com.necpgame.backjava.model.TradingGuild;
import com.necpgame.backjava.model.TradingGuildDetailed;
import com.necpgame.backjava.model.UpgradeGuildRequest;
import com.necpgame.backjava.service.TradingGuildsService;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TradingGuildsServiceImpl implements TradingGuildsService {

    private UnsupportedOperationException error() {
        return new UnsupportedOperationException("Trading guilds service is not implemented yet");
    }

    @Override
    public Void addGuildMember(UUID guildId, AddGuildMemberRequest addGuildMemberRequest) {
        throw error();
    }

    @Override
    public TradingGuild createTradingGuild(CreateGuildRequest createGuildRequest) {
        throw error();
    }

    @Override
    public GetGuildMembers200Response getGuildMembers(UUID guildId) {
        throw error();
    }

    @Override
    public TradingGuildDetailed getTradingGuild(UUID guildId) {
        throw error();
    }

    @Override
    public ListTradingGuilds200Response listTradingGuilds(String type, Integer minLevel, String region, Integer page, Integer pageSize) {
        throw error();
    }

    @Override
    public Void upgradeGuild(UUID guildId, UpgradeGuildRequest upgradeGuildRequest) {
        throw error();
    }
}


