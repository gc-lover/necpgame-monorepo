package com.necpgame.backjava.controller;

import com.necpgame.backjava.api.MvpApi;
import com.necpgame.backjava.model.ContentOverview;
import com.necpgame.backjava.model.ContentStatus;
import com.necpgame.backjava.model.GetMVPEndpoints200Response;
import com.necpgame.backjava.model.GetMVPHealth200Response;
import com.necpgame.backjava.model.GetMVPModels200Response;
import com.necpgame.backjava.model.InitialGameData;
import com.necpgame.backjava.model.MainGameUIData;
import com.necpgame.backjava.model.TextVersionState;
import com.necpgame.backjava.service.MvpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class MvpController implements MvpApi {

    private final MvpService mvpService;

    @Override
    public ResponseEntity<GetMVPEndpoints200Response> getMVPEndpoints() {
        return ResponseEntity.ok(mvpService.getMVPEndpoints());
    }

    @Override
    public ResponseEntity<GetMVPModels200Response> getMVPModels() {
        return ResponseEntity.ok(mvpService.getMVPModels());
    }

    @Override
    public ResponseEntity<InitialGameData> getInitialData() {
        return ResponseEntity.ok(mvpService.getInitialData());
    }

    @Override
    public ResponseEntity<ContentOverview> getContentOverview(String period) {
        return ResponseEntity.ok(mvpService.getContentOverview(period));
    }

    @Override
    public ResponseEntity<ContentStatus> getContentStatus() {
        return ResponseEntity.ok(mvpService.getContentStatus());
    }

    @Override
    public ResponseEntity<TextVersionState> getTextVersionState(UUID characterId) {
        return ResponseEntity.ok(mvpService.getTextVersionState(characterId));
    }

    @Override
    public ResponseEntity<MainGameUIData> getMainGameUI(UUID characterId) {
        return ResponseEntity.ok(mvpService.getMainGameUI(characterId));
    }

    @Override
    public ResponseEntity<GetMVPHealth200Response> getMVPHealth() {
        return ResponseEntity.ok(mvpService.getMVPHealth());
    }
}



