package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.LoreApi;
import com.necpgame.backjava.model.FactionDetailed;
import com.necpgame.backjava.model.GetCharacterCategories200Response;
import com.necpgame.backjava.model.GetCharacterCodex200Response;
import com.necpgame.backjava.model.GetTimeline200Response;
import com.necpgame.backjava.model.ListFactions200Response;
import com.necpgame.backjava.model.ListLocations200Response;
import com.necpgame.backjava.model.LocationDetailed;
import com.necpgame.backjava.model.SearchLore200Response;
import com.necpgame.backjava.model.UniverseLore;
import com.necpgame.backjava.model.UnlockCodexEntryRequest;
import com.necpgame.backjava.service.LoreService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoreController implements LoreApi {

    private final LoreService loreService;

    @Override
    public ResponseEntity<GetCharacterCategories200Response> getCharacterCategories() {
        return ResponseEntity.ok(loreService.getCharacterCategories());
    }

    @Override
    public ResponseEntity<GetCharacterCodex200Response> getCharacterCodex(UUID characterId) {
        return ResponseEntity.ok(loreService.getCharacterCodex(characterId));
    }

    @Override
    public ResponseEntity<FactionDetailed> getFaction(String factionId) {
        return ResponseEntity.ok(loreService.getFaction(factionId));
    }

    @Override
    public ResponseEntity<LocationDetailed> getLocation(String locationId) {
        return ResponseEntity.ok(loreService.getLocation(locationId));
    }

    @Override
    public ResponseEntity<GetTimeline200Response> getTimeline(@Nullable String era, @Nullable String eventType) {
        return ResponseEntity.ok(loreService.getTimeline(era, eventType));
    }

    @Override
    public ResponseEntity<UniverseLore> getUniverseLore() {
        return ResponseEntity.ok(loreService.getUniverseLore());
    }

    @Override
    public ResponseEntity<ListFactions200Response> listFactions(
        @Nullable String type,
        @Nullable String region,
        @Nullable Integer page,
        @Nullable Integer pageSize
    ) {
        return ResponseEntity.ok(loreService.listFactions(type, region, page, pageSize));
    }

    @Override
    public ResponseEntity<ListLocations200Response> listLocations(
        @Nullable String region,
        @Nullable String type,
        @Nullable Integer page,
        @Nullable Integer pageSize
    ) {
        return ResponseEntity.ok(loreService.listLocations(region, type, page, pageSize));
    }

    @Override
    public ResponseEntity<SearchLore200Response> searchLore(String q, @Nullable String category) {
        return ResponseEntity.ok(loreService.searchLore(q, category));
    }

    @Override
    public ResponseEntity<Void> unlockCodexEntry(UnlockCodexEntryRequest unlockCodexEntryRequest) {
        loreService.unlockCodexEntry(unlockCodexEntryRequest);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

