package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.TradingGuildsApi;
import com.necpgame.backjava.model.AddGuildMemberRequest;
import com.necpgame.backjava.model.CreateGuildRequest;
import com.necpgame.backjava.model.GetGuildMembers200Response;
import com.necpgame.backjava.model.ListTradingGuilds200Response;
import com.necpgame.backjava.model.TradingGuild;
import com.necpgame.backjava.model.TradingGuildDetailed;
import com.necpgame.backjava.model.UpgradeGuildRequest;
import com.necpgame.backjava.service.TradingGuildsService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TradingGuildsController implements TradingGuildsApi {

    private final TradingGuildsService tradingGuildsService;

    @Override
    public ResponseEntity<Void> addGuildMember(UUID guildId, AddGuildMemberRequest addGuildMemberRequest) {
        tradingGuildsService.addGuildMember(guildId, addGuildMemberRequest);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<TradingGuild> createTradingGuild(CreateGuildRequest createGuildRequest) {
        TradingGuild response = tradingGuildsService.createTradingGuild(createGuildRequest);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<GetGuildMembers200Response> getGuildMembers(UUID guildId) {
        GetGuildMembers200Response response = tradingGuildsService.getGuildMembers(guildId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<TradingGuildDetailed> getTradingGuild(UUID guildId) {
        TradingGuildDetailed detailed = tradingGuildsService.getTradingGuild(guildId);
        return ResponseEntity.ok(detailed);
    }

    @Override
    public ResponseEntity<ListTradingGuilds200Response> listTradingGuilds(String type, Integer minLevel, String region,
                                                                          Integer page, Integer pageSize) {
        ListTradingGuilds200Response response = tradingGuildsService.listTradingGuilds(type, minLevel, region, page, pageSize);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> upgradeGuild(UUID guildId, UpgradeGuildRequest upgradeGuildRequest) {
        tradingGuildsService.upgradeGuild(guildId, upgradeGuildRequest);
        return ResponseEntity.ok().build();
    }
}

