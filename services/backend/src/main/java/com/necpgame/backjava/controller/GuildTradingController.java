package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.GuildTradingApi;
import com.necpgame.backjava.model.GetGuildQuotas200Response;
import com.necpgame.backjava.model.GetGuildTradeRoutes200Response;
import com.necpgame.backjava.service.GuildTradingService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GuildTradingController implements GuildTradingApi {

    private final GuildTradingService guildTradingService;

    @Override
    public ResponseEntity<GetGuildQuotas200Response> getGuildQuotas(UUID guildId) {
        GetGuildQuotas200Response response = guildTradingService.getGuildQuotas(guildId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GetGuildTradeRoutes200Response> getGuildTradeRoutes(UUID guildId) {
        GetGuildTradeRoutes200Response response = guildTradingService.getGuildTradeRoutes(guildId);
        return ResponseEntity.ok(response);
    }
}

