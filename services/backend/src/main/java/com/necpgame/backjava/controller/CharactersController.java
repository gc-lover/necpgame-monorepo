package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.CharactersApi;
import com.necpgame.backjava.model.CharacterActivityListResponse;
import com.necpgame.backjava.model.CharacterAppearancePatch;
import com.necpgame.backjava.model.CharacterAppearanceResponse;
import com.necpgame.backjava.model.CharacterCreateRequest;
import com.necpgame.backjava.model.CharacterCreateResponse;
import com.necpgame.backjava.model.CharacterDeleteResponse;
import com.necpgame.backjava.model.CharacterListResponse;
import com.necpgame.backjava.model.CharacterRecalculateResponse;
import com.necpgame.backjava.model.CharacterRestoreRequest;
import com.necpgame.backjava.model.CharacterRestoreResponse;
import com.necpgame.backjava.model.CharacterSlotPurchaseRequest;
import com.necpgame.backjava.model.CharacterSlotPurchaseResponse;
import com.necpgame.backjava.model.CharacterSlotStateResponse;
import com.necpgame.backjava.model.CharacterStatsRecalculateRequest;
import com.necpgame.backjava.model.CharacterSwitchRequest;
import com.necpgame.backjava.model.CharacterSwitchResponse;
import com.necpgame.backjava.service.CharactersService;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CharactersController implements CharactersApi {

    private final CharactersService charactersService;

    @Override
    public ResponseEntity<CharacterActivityListResponse> charactersPlayersAccountsAccountIdActivityGet(UUID accountId,
                                                                                                      String activityType,
                                                                                                      OffsetDateTime dateFrom,
                                                                                                      OffsetDateTime dateTo,
                                                                                                      Integer page,
                                                                                                      Integer pageSize) {
        log.info("GET /characters/players/accounts/{}/activity", accountId);
        CharacterActivityListResponse response = charactersService
            .charactersPlayersAccountsAccountIdActivityGet(accountId, activityType, dateFrom, dateTo, page, pageSize);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CharacterAppearanceResponse> charactersPlayersAccountsAccountIdCharactersCharacterIdAppearancePatch(UUID accountId,
                                                                                                                            UUID characterId,
                                                                                                                            CharacterAppearancePatch characterAppearancePatch) {
        log.info("PATCH /characters/players/accounts/{}/characters/{}/appearance", accountId, characterId);
        CharacterAppearanceResponse response = charactersService
            .charactersPlayersAccountsAccountIdCharactersCharacterIdAppearancePatch(accountId, characterId, characterAppearancePatch);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CharacterDeleteResponse> charactersPlayersAccountsAccountIdCharactersCharacterIdDelete(UUID accountId,
                                                                                                                UUID characterId) {
        log.info("DELETE /characters/players/accounts/{}/characters/{}", accountId, characterId);
        CharacterDeleteResponse response = charactersService
            .charactersPlayersAccountsAccountIdCharactersCharacterIdDelete(accountId, characterId);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Override
    public ResponseEntity<CharacterRecalculateResponse> charactersPlayersAccountsAccountIdCharactersCharacterIdRecalculatePost(UUID accountId,
                                                                                                                             UUID characterId,
                                                                                                                             CharacterStatsRecalculateRequest characterStatsRecalculateRequest) {
        log.info("POST /characters/players/accounts/{}/characters/{}/recalculate", accountId, characterId);
        CharacterRecalculateResponse response = charactersService
            .charactersPlayersAccountsAccountIdCharactersCharacterIdRecalculatePost(accountId, characterId, characterStatsRecalculateRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Override
    public ResponseEntity<CharacterRestoreResponse> charactersPlayersAccountsAccountIdCharactersCharacterIdRestorePost(UUID accountId,
                                                                                                                       UUID characterId,
                                                                                                                       CharacterRestoreRequest characterRestoreRequest) {
        log.info("POST /characters/players/accounts/{}/characters/{}/restore", accountId, characterId);
        CharacterRestoreResponse response = charactersService
            .charactersPlayersAccountsAccountIdCharactersCharacterIdRestorePost(accountId, characterId, characterRestoreRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CharacterListResponse> charactersPlayersAccountsAccountIdCharactersGet(UUID accountId,
                                                                                                 Boolean includeDeleted,
                                                                                                 Boolean includeSnapshots) {
        log.info("GET /characters/players/accounts/{}/characters", accountId);
        CharacterListResponse response = charactersService
            .charactersPlayersAccountsAccountIdCharactersGet(accountId, includeDeleted, includeSnapshots);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CharacterCreateResponse> charactersPlayersAccountsAccountIdCharactersPost(UUID accountId,
                                                                                                    CharacterCreateRequest characterCreateRequest) {
        log.info("POST /characters/players/accounts/{}/characters", accountId);
        CharacterCreateResponse response = charactersService
            .charactersPlayersAccountsAccountIdCharactersPost(accountId, characterCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<CharacterSlotStateResponse> charactersPlayersAccountsAccountIdSlotsGet(UUID accountId) {
        log.info("GET /characters/players/accounts/{}/slots", accountId);
        CharacterSlotStateResponse response = charactersService
            .charactersPlayersAccountsAccountIdSlotsGet(accountId);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<CharacterSlotPurchaseResponse> charactersPlayersAccountsAccountIdSlotsPurchasePost(UUID accountId,
                                                                                                             CharacterSlotPurchaseRequest characterSlotPurchaseRequest) {
        log.info("POST /characters/players/accounts/{}/slots/purchase", accountId);
        CharacterSlotPurchaseResponse response = charactersService
            .charactersPlayersAccountsAccountIdSlotsPurchasePost(accountId, characterSlotPurchaseRequest);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @Override
    public ResponseEntity<CharacterSwitchResponse> charactersPlayersAccountsAccountIdSwitchPost(UUID accountId,
                                                                                                CharacterSwitchRequest characterSwitchRequest) {
        log.info("POST /characters/players/accounts/{}/switch", accountId);
        CharacterSwitchResponse response = charactersService
            .charactersPlayersAccountsAccountIdSwitchPost(accountId, characterSwitchRequest);
        return ResponseEntity.ok(response);
    }
}
