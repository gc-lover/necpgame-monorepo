package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.PlayersApi;
import com.necpgame.backjava.model.CreatePlayerCharacterRequest;
import com.necpgame.backjava.model.DeletePlayerCharacter200Response;
import com.necpgame.backjava.model.GetCharacters200Response;
import com.necpgame.backjava.model.PlayerCharacter;
import com.necpgame.backjava.model.PlayerCharacterDetails;
import com.necpgame.backjava.model.PlayerProfile;
import com.necpgame.backjava.model.SwitchCharacter200Response;
import com.necpgame.backjava.model.SwitchCharacterRequest;
import com.necpgame.backjava.service.PlayersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PlayersController implements PlayersApi {

    private final PlayersService playersService;

    @Override
    public ResponseEntity<PlayerProfile> getPlayerProfile() {
        return ResponseEntity.ok(playersService.getPlayerProfile());
    }

    @Override
    public ResponseEntity<GetCharacters200Response> getCharacters(Boolean includeDeleted) {
        return ResponseEntity.ok(playersService.getCharacters(includeDeleted));
    }

    @Override
    public ResponseEntity<PlayerCharacter> createPlayerCharacter(CreatePlayerCharacterRequest createPlayerCharacterRequest) {
        PlayerCharacter created = playersService.createPlayerCharacter(createPlayerCharacterRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Override
    public ResponseEntity<PlayerCharacterDetails> getCharacter(String characterId) {
        return ResponseEntity.ok(playersService.getCharacter(characterId));
    }

    @Override
    public ResponseEntity<DeletePlayerCharacter200Response> deletePlayerCharacter(String characterId) {
        return ResponseEntity.ok(playersService.deletePlayerCharacter(characterId));
    }

    @Override
    public ResponseEntity<PlayerCharacter> restoreCharacter(String characterId) {
        return ResponseEntity.ok(playersService.restoreCharacter(characterId));
    }

    @Override
    public ResponseEntity<SwitchCharacter200Response> switchCharacter(SwitchCharacterRequest switchCharacterRequest) {
        return ResponseEntity.ok(playersService.switchCharacter(switchCharacterRequest));
    }
}


