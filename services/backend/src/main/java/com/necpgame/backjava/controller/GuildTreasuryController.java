package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.GuildTreasuryApi;
import com.necpgame.backjava.model.ContributeToTreasury200Response;
import com.necpgame.backjava.model.ContributionRequest;
import com.necpgame.backjava.model.DistributeProfits200Response;
import com.necpgame.backjava.model.DistributeProfitsRequest;
import com.necpgame.backjava.model.GuildTreasury;
import com.necpgame.backjava.service.GuildTreasuryService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GuildTreasuryController implements GuildTreasuryApi {

    private final GuildTreasuryService guildTreasuryService;

    @Override
    public ResponseEntity<ContributeToTreasury200Response> contributeToTreasury(UUID guildId, ContributionRequest contributionRequest) {
        ContributeToTreasury200Response response = guildTreasuryService.contributeToTreasury(guildId, contributionRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<DistributeProfits200Response> distributeProfits(UUID guildId, DistributeProfitsRequest distributeProfitsRequest) {
        DistributeProfits200Response response = guildTreasuryService.distributeProfits(guildId, distributeProfitsRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GuildTreasury> getGuildTreasury(UUID guildId) {
        GuildTreasury treasury = guildTreasuryService.getGuildTreasury(guildId);
        return ResponseEntity.ok(treasury);
    }
}

